package org.shareio.backend.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class AuthenticationHandler {

    @Getter
    @Value("${spring.profiles.active}")
    private String activeProfile;

    private final IdentityHandler identityHandler;
    private final PermissionHandler permissionHandler;
    private final ProfileHandler profileHandler;

    private final String messageEnd = "------------------------------------";

    public AuthenticationHandler(IdentityHandler identityHandler, PermissionHandler permissionHandler, ProfileHandler profileHandler) {
        this.identityHandler = identityHandler;
        this.permissionHandler = permissionHandler;
        this.profileHandler = profileHandler;
    }

    public boolean authenticateRequestForUserIdentity(HttpServletRequest httpRequest, UUID userId) {
        String logMessage = "\n------- AUTHENTICATE REQUEST -------\n";
        if(checkIfTestProfile()){
            return true;
        }
        if (checkAdminRole(httpRequest)) {
            return true;
        }
        logMessage+=("IsSameUser: "+ identityHandler.isSameUser(httpRequest, userId)+"\n");
        if (permissionHandler.isUser(httpRequest) && identityHandler.isSameUser(httpRequest, userId)) {
            logMessage+=("Authentication sucessful for user "+ userId+"\n");
            logMessage+=(messageEnd);
            log.error(logMessage);
            return true;
        } else {
            logMessage+=("Authentication failed for user "+ httpRequest.getHeaders("id").nextElement()+": Identity mismatch!\n");
            logMessage+=(messageEnd);
            log.error(logMessage);
            return false;
        }

    }

    public boolean authenticateRequest(HttpServletRequest httpRequest, UUID userId) {
        String logMessage = "\n------- AUTHENTICATE REQUEST -------\n";
        logMessage+= "Requested endpoint: " + httpRequest.getRequestURI() + "\n";
        if(checkIfTestProfile()){
            return true;
        }
        if(checkAdminRole(httpRequest)){
            return true;
        }
        if(permissionHandler.isUser(httpRequest)){
            logMessage+=("Authentication sucessful for user "+ userId+"\n");
            logMessage+=(messageEnd);
            log.error(logMessage);
            return true;
        }
        else {
            logMessage+=("Authentication failed for user "+ userId+"\n");
            logMessage+=(messageEnd);
            log.error(logMessage);
            return false;
        }
        }

    private boolean checkIfTestProfile() {
        String logMessage = "\n------- VERIFY PROFILE -------\n";
        logMessage += ("TestProfile: " + profileHandler.checkTestProfile(activeProfile) + "\n");
        logMessage += ("ProdProfile: " + profileHandler.checkProdProfile(activeProfile) + "\n");
        if (profileHandler.checkTestProfile(activeProfile)) {
            logMessage += ("Test profile - skipping authentication" + "\n");
            logMessage += (messageEnd);
            log.error(logMessage);
            return true;
        }

        else {
            logMessage += ("Production profile" + "\n");
            logMessage += (messageEnd);
            log.error(logMessage);
            return false;
        }
    }

    private boolean checkAdminRole(HttpServletRequest httpRequest){
        String logMessage = "\n------- VERIFY PERMISSION -------\n";
        logMessage+=("IsAdmin: "+ permissionHandler.isAdmin(httpRequest)+"\n");
        logMessage+=("IsUser: "+ permissionHandler.isUser(httpRequest)+"\n");
        if (permissionHandler.isAdmin(httpRequest)){
            logMessage+=("Admin permission - skipping authentication\n");
            logMessage+=(messageEnd);
            log.error(logMessage);
            return true;
        }
        else {
            logMessage+=("Non-admin permission\n");
            logMessage+=(messageEnd);
            log.error(logMessage);
            return false;
        }
    }
}