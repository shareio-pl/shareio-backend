package org.shareio.backend.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@NoArgsConstructor
@Service
public class PermissionHandler {

    public boolean isUser(HttpServletRequest httpRequest){

        if(!httpRequest.getHeaders("role").asIterator().hasNext()){
            return false;
        }
        return Objects.equals(httpRequest.getHeaders("role").nextElement(),"USER");
    }

    public boolean isAdmin(HttpServletRequest httpRequest){
        if(!httpRequest.getHeaders("role").asIterator().hasNext()){
            return false;
        }
        return Objects.equals(httpRequest.getHeaders("role").nextElement(),"ADMIN");
    }
}
