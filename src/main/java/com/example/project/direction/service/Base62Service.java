package com.example.project.direction.service;


import io.seruco.encoding.base62.Base62;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class Base62Service {
    private static final Base62 base63Instance = Base62.createInstance();

    public String encodeDirectionId(Long directionId){
        return new String(base63Instance.encode(String.valueOf(directionId).getBytes()));
    }


    public Long decodeDirectionId(String encodingDirectionId){
        String resultDirectionId = new String(base63Instance.decode(encodingDirectionId.getBytes()));

        return Long.valueOf(resultDirectionId);
    }
}
