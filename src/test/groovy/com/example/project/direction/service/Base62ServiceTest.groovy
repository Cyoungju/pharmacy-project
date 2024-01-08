package com.example.project.direction.service

import spock.lang.Specification

class Base62ServiceTest extends Specification {
    private Base62Service base62Service

    def setup(){
        base62Service = new Base62Service()
    }

    def "check base62 encoding/decoding"(){
        given:
        long num = 5;

        when:
        def encodingId = base62Service.encodeDirectionId(num)

        def decodeId = base62Service.decodeDirectionId(encodingId)

        then:
        num == decodeId

    }
}
