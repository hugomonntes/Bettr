<?php
error_reporting(E_ERROR | E_PARSE);

class ApiClient
{
    private $baseUrl;
    
    public function __construct()
    {
        $this->baseUrl = "https://bettr-g5yv.onrender.com/rest";
    }
    
    private function request($method, $endpoint, $data = null)
    {
        $url = rtrim($this->baseUrl . '/' . $endpoint, '/');
        
        $ch = curl_init($url);
        
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true);
        curl_setopt($ch, CURLOPT_TIMEOUT, 30);
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, true);
        
        $method = strtoupper($method);
        
        if ($method === 'POST') {
            curl_setopt($ch, CURLOPT_POST, true);
            
            if ($data) {
                curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($data));
            }
            
            $headers = [
                'Content-Type: application/json',
                'Accept: application/json'
            ];
            curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
            
        } elseif ($method === 'PUT') {
            curl_setopt($ch, CURLOPT_CUSTOMREQUEST, 'PUT');
            
            if ($data) {
                curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($data));
            }
            
            $headers = [
                'Content-Type: application/json',
                'Accept: application/json'
            ];
            curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
            
        } elseif ($method === 'DELETE') {
            curl_setopt($ch, CURLOPT_CUSTOMREQUEST, 'DELETE');
            
            if ($data) {
                curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($data));
            }
            
            $headers = [
                'Content-Type: application/json',
                'Accept: application/json'
            ];
            curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
            
        } else {
            curl_setopt($ch, CURLOPT_HTTPHEADER, ['Accept: application/json']);
        }
        
        $response = curl_exec($ch);
        $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
        $error = curl_error($ch);
        
        error_log("API Request - Method: $method, URL: $url, HTTP Code: $httpCode");
        
        if ($error) {
            error_log("cURL Error: $error");
            throw new Exception("Error de conexión: " . $error);
        }
        
        $decoded = json_decode($response, true);
        
        return [
            'status' => $httpCode,
            'data' => $decoded,
            'raw' => $response
        ];
    }
    
    public function get($endpoint, $params = [])
    {
        if (!empty($params)) {
            $endpoint .= '?' . http_build_query($params);
        }
        
        return $this->request('GET', $endpoint);
    }
    
    public function post($endpoint, $data)
    {
        return $this->request('POST', $endpoint, $data);
    }
    
    public function put($endpoint, $data)
    {
        return $this->request('PUT', $endpoint, $data);
    }
    
    public function delete($endpoint)
    {
        return $this->request('DELETE', $endpoint);
    }
    
    public function deleteWithBody($endpoint, $data = [])
    {
        return $this->request('DELETE', $endpoint, $data);
    }
}

