<?php
/**
 * Bettr API Client
 * 
 * Clase genérica para manejar todas las peticiones HTTP a la API REST
 * Usa cURL para comunicar con el servidor backend
 */

// Report only serious errors
error_reporting(E_ERROR | E_PARSE);

class ApiClient
{
    // URL base del servidor donde está la API
    private $baseUrl;
    
    // Constructor: configura la URL de la API
    public function __construct()
    {
        $this->baseUrl = "https://bettr-g5yv.onrender.com/rest";
    }
    
    /**
     * Método genérico request()
     * Centraliza TODAS las peticiones HTTP usando cURL
     * 
     * @param string $method - GET/POST/PUT/DELETE
     * @param string $endpoint - endpoint de la API (sin la base URL)
     * @param mixed $data - datos opcionales (para POST/PUT)
     * @return mixed - respuesta de la API decodeada como array/objeto
     */
    private function request($method, $endpoint, $data = null)
    {
        // Construir la URL completa
        $url = rtrim($this->baseUrl . '/' . $endpoint, '/');
        
        // Inicializar cURL
        $ch = curl_init($url);
        
        // Configurar opciones de cURL
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true);
        curl_setopt($ch, CURLOPT_TIMEOUT, 30);
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, true);
        
        // Definir el método HTTP dinámicamente
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
            // GET
            curl_setopt($ch, CURLOPT_HTTPHEADER, ['Accept: application/json']);
        }
        
        // Ejecutar la petición HTTP
        $response = curl_exec($ch);
        $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
        $error = curl_error($ch);
        
        // Cerrar la conexión
        // curl_close($ch); // Not needed in PHP 8.0+, deprecated in 8.5
        
        // Log para debugging
        error_log("API Request - Method: $method, URL: $url, HTTP Code: $httpCode");
        
        if ($error) {
            error_log("cURL Error: $error");
            throw new Exception("Error de conexión: " . $error);
        }
        
        // Decodificar la respuesta JSON
        $decoded = json_decode($response, true);
        
        // Devolver respuesta con código HTTP
        return [
            'status' => $httpCode,
            'data' => $decoded,
            'raw' => $response
        ];
    }
    
    // =============================================
    // MÉTODOS PÚBLICOS CRUD
    // =============================================
    
    // GET - Obtener recursos
    public function get($endpoint, $params = [])
    {
        // Agregar query params si existen
        if (!empty($params)) {
            $endpoint .= '?' . http_build_query($params);
        }
        
        return $this->request('GET', $endpoint);
    }
    
    // POST - Crear nuevo recurso
    public function post($endpoint, $data)
    {
        return $this->request('POST', $endpoint, $data);
    }
    
    // PUT - Actualizar recurso
    public function put($endpoint, $data)
    {
        return $this->request('PUT', $endpoint, $data);
    }
    
    // DELETE - Eliminar recurso
    public function delete($endpoint)
    {
        return $this->request('DELETE', $endpoint);
    }
    
    // Method to make custom DELETE request with body support
    public function deleteWithBody($endpoint, $data = [])
    {
        return $this->request('DELETE', $endpoint, $data);
    }
}

