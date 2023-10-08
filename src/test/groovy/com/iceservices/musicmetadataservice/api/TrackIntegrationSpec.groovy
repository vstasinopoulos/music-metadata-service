package com.iceservices.musicmetadataservice.api

import com.iceservices.musicmetadataservice.AbstractIntegrationSpec
import com.iceservices.musicmetadataservice.api.response.PageableResponseDto
import com.iceservices.musicmetadataservice.api.response.TrackResponseDto
import io.restassured.RestAssured
import io.restassured.http.ContentType
import spock.lang.Shared

class TrackIntegrationSpec extends AbstractIntegrationSpec {

    @Shared
    String artistId

    def setup() {
        artistId = RestAssured.given()
                .body('''{"name": "a-name", "nameAliases": ["an-alias", "another-alias"]}''')
                .contentType(ContentType.JSON)
                .post('v1/artists')
                .header('Location')
                .substring(9)
    }

    def "should create track - all fields"() {
        given:
        def track = """{"title": "a-title", "genre": "other", "length": "PT3M35.123456789S", "artistId": "$artistId"}"""
        def req = RestAssured.given().body(track).contentType(ContentType.JSON)

        when:
        def res = req.post('v1/tracks')

        then:
        res.statusCode == 201
        res.header('Location') ==~ /\/tracks\// + UUID_REGEX
    }

    def "should create track - mandatory fields"() {
        given:
        def track = """{"title": "a-title", "artistId": "$artistId"}"""
        def req = RestAssured.given().body(track).contentType(ContentType.JSON)

        when:
        def res = req.post('v1/tracks')

        then:
        res.statusCode == 201
        res.header('Location') ==~ /\/tracks\// + UUID_REGEX
    }

    def "should not create track - missing title"() {
        given:
        def track = """{"genre": "other", "length": "PT3M35.123456789S", "artistId": "$artistId"}"""
        def req = RestAssured.given().body(track).contentType(ContentType.JSON)

        when:
        def res = req.post('v1/tracks')

        then:
        res.statusCode == 400
    }

    def "should not create track - missing artistId"() {
        given:
        def track = """{"title": "a-title", "genre": "other", "length": "PT3M35.123456789S"}"""
        def req = RestAssured.given().body(track).contentType(ContentType.JSON)

        when:
        def res = req.post('v1/tracks')

        then:
        res.statusCode == 400
    }

    def "should get all tracks of an artist"() {
        given:
        def track1 = """{"title": "a-title-1", "genre": "rock", "length": "PT3M25.623656689S", "artistId": "$artistId"}"""
        RestAssured.given().body(track1).contentType(ContentType.JSON).post('v1/tracks')
        def track2 = """{"title": "a-title-2", "genre": "electronic", "length": "PT14M85.923466769S", "artistId": "$artistId"}"""
        RestAssured.given().body(track2).contentType(ContentType.JSON).post('v1/tracks')
        def track3 = """{"title": "a-title-3", "genre": "classical", "length": "PT9M55.523456789S", "artistId": "$artistId"}"""
        RestAssured.given().body(track3).contentType(ContentType.JSON).post('v1/tracks')

        when:
        def res = RestAssured.given().get("v1/tracks/$artistId")

        then:
        res.statusCode == 200
        with(res.body.as(PageableResponseDto)) {
            (!hasMore)
            data.size() == 3
            with(data[0]) {
                title == "a-title-1"
                genre == "rock"
                length == "PT3M25.623656689S"
                created != null
                modified != null
            }
        }

        when:
        res = RestAssured.given().get("v1/tracks/$artistId?pageNumber=0&pageSize=2")

        then:
        res.statusCode == 200
        with(res.body.as(PageableResponseDto<TrackResponseDto>)) {
            hasMore
            data.size() == 2
            with(data[0]) {
                title == "a-title-1"
                genre == "rock"
                length == "PT3M25.623656689S"
                created != null
                modified != null
            }
            with(nextPage) {
                pageSize == 2
                pageNumber == 1
            }
        }
    }

}
