package org.shareio.backend.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@Service
public class IdentityHandler {

    public boolean isSameUser(HttpServletRequest httpRequest, UUID userId) {
        if(!httpRequest.getHeaders("role").asIterator().hasNext()){
            return false;
        }
        return Objects.equals(httpRequest.getHeaders("id").nextElement(),userId.toString());
    }

    public UUID getUserIdFromHeader(HttpServletRequest httpRequest){
        if(!httpRequest.getHeaders("id").asIterator().hasNext()){
            return null;
        }
        return UUID.fromString(httpRequest.getHeaders("id").nextElement());
    }
}
