package in.maven.ark.lms.ide.service;

import in.maven.ark.lms.ide.dto.IdeSessionRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class GoldIdeClient {

    @Value("${gold-ide.base-url}")
    private String goldIdeBaseUrl;

    @Value("${gold-ide.api-key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String createWorkspace(IdeSessionRequest request) {
        String url = goldIdeBaseUrl + "/api/workspaces";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);
        
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("repositoryUrl", request.getRepositoryUrl());
        requestBody.put("branch", request.getBranch());
        requestBody.put("userId", request.getUserId());
        requestBody.put("projectId", request.getProjectId());
        requestBody.put("workspaceConfig", request.getWorkspaceConfig());
        
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        
        Map<String, Object> response = restTemplate.postForObject(url, entity, Map.class);
        
        if (response != null && response.containsKey("sessionId")) {
            return (String) response.get("sessionId");
        }
        
        throw new RuntimeException("Failed to create Gold-IDE workspace");
    }

    public void closeWorkspace(String sessionId) {
        String url = goldIdeBaseUrl + "/api/workspaces/" + sessionId + "/close";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        
        restTemplate.postForObject(url, entity, Void.class);
    }

    public void extendWorkspace(String sessionId, int additionalMinutes) {
        String url = goldIdeBaseUrl + "/api/workspaces/" + sessionId + "/extend";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);
        
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("additionalMinutes", additionalMinutes);
        
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        
        restTemplate.postForObject(url, entity, Void.class);
    }

    public Map<String, Object> getWorkspaceStatus(String sessionId) {
        String url = goldIdeBaseUrl + "/api/workspaces/" + sessionId + "/status";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        
        return restTemplate.getForObject(url, Map.class);
    }

    public String getGoldIdeBaseUrl() {
        return goldIdeBaseUrl;
    }
}
