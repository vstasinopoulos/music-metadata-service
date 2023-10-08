package com.iceservices.musicmetadataservice.api

import com.iceservices.musicmetadataservice.AbstractIntegrationSpec
import io.restassured.RestAssured
import io.restassured.http.ContentType

class ArtistIntegrationSpec extends AbstractIntegrationSpec {

    def "should create artist with payload: #artist"() {
        given:
        def req = RestAssured.given().body(artist).contentType(ContentType.JSON)

        when:
        def res = req.post('v1/artists')

        then:
        res.statusCode == 201
        res.header('Location') ==~ /\/artists\// + UUID_REGEX

        where:
        artist << ['''{"name": "a-name", "nameAliases": ["an-alias", "another-alias"]}''', '''{"name": "another-name"}''']
    }

    def "should not create (validation error) artist with payload: #artist"() {
        given:
        def req = RestAssured.given().body(artist).contentType(ContentType.JSON)

        when:
        def res = req.post('v1/artists')

        then:
        res.statusCode == 400

        where:
        artist << ['''{"nameAliases": ["an-alias", "another-alias"]}''', ""]
    }

    def "should add artist name aliases to artist: #artist"() {
        given:
        def id = RestAssured.given().body(artist).contentType(ContentType.JSON).post('v1/artists').header('Location').substring(9) // Leave out /artists/ part
        def req = RestAssured.given().body('''{"nameAliases": ["an-alias-2", "another-alias-2"]}''').contentType(ContentType.JSON)

        when:
        def res = req.patch("v1/artists/${id}")

        then:
        res.statusCode == 200

        where:
        artist << ['''{"name": "a-name", "nameAliases": ["an-alias", "another-alias"]}''', '''{"name": "another-name"}''']

    }

    def "should not add (validation error) artist name aliases"() {
        given:
        def id = RestAssured.given().body('''{"name": "a-name"}''').contentType(ContentType.JSON).post('v1/artists').header('Location').substring(9)
        def req = RestAssured.given().contentType(ContentType.JSON)

        when:
        def res = req.patch("v1/artists/${id}")

        then:
        res.statusCode == 400
    }

    def "should get the artist of the day"() {
        given:
        def id = RestAssured.given().body('''{"name": "a-name"}''').contentType(ContentType.JSON).post('v1/artists').header('Location').substring(9)
        def req = RestAssured.given().contentType(ContentType.JSON)

        when:
        def res = req.get("v1/artists/artist-of-the-day")

        then:
        res.statusCode == 200
    }

    // TODO Add more tests (both happy path and failing scenarios) for the GET v1/artists/artist-of-the-day by overwriting the system clock
}
