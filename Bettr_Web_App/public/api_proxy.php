<?php
/**
 * Bettr API Proxy
 * 
 * Usa la clase ApiClient para manejar las peticiones a la API REST
 * Esto evita CORS y centraliza la comunicación con el backend
 */

// Incluir la clase API Client
require_once __DIR__ . '/../app/services/ApiClient.php';

// Configurar headers
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, POST, OPTIONS');
header('Access-Control-Allow-Headers: Content-Type, Accept');

// Manejar OPTIONS preflight
if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit;
}

// Obtener método y endpoint
$method = $_SERVER['REQUEST_METHOD'];
$endpoint = $_GET['endpoint'] ?? '';

// Obtener datos del body (para POST/PUT)
$input = file_get_contents('php://input');
$data = json_decode($input, true);

// Inicializar API Client
$api = new ApiClient();

try {
    // Realizar la petición según el método
    switch ($method) {
        case 'GET':
            // GET puede tener query params adicionales
            $params = $_GET;
            unset($params['endpoint']);
            $result = $api->get($endpoint, $params);
            break;
            
        case 'POST':
            $result = $api->post($endpoint, $data);
            break;
            
        case 'PUT':
            $result = $api->put($endpoint, $data);
            break;
            
        case 'DELETE':
            $result = $api->delete($endpoint);
            break;
            
        default:
            http_response_code(405); // Method Not Allowed
            echo json_encode(['error' => 'Método no permitido']);
            exit;
    }
    
    // Devolver el código HTTP de la respuesta
    http_response_code($result['status']);
    
    // Devolver los datos de la respuesta
    echo json_encode($result['data']);
    
} catch (Exception $e) {
    error_log("API Proxy Error: " . $e->getMessage());
    http_response_code(500);
    echo json_encode([
        'error' => true,
        'message' => 'Error de conexión con la API',
        'details' => $e->getMessage()
    ]);
}

