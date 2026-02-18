<?php
error_reporting(E_ERROR | E_PARSE);

require_once __DIR__ . '/../app/services/ApiClient.php';

header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS');
header('Access-Control-Allow-Headers: Content-Type, Accept, X-HTTP-Method-Override');

if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit;
}

$method = $_SERVER['REQUEST_METHOD'];
if (isset($_SERVER['HTTP_X_HTTP_METHOD_OVERRIDE'])) {
    $method = strtoupper($_SERVER['HTTP_X_HTTP_METHOD_OVERRIDE']);
}
$endpoint = $_GET['endpoint'] ?? '';

$input = file_get_contents('php://input');
$data = json_decode($input, true);

$api = new ApiClient();

try {
    switch ($method) {
        case 'GET':
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
            $result = $api->deleteWithBody($endpoint, $data);
            break;
            
        default:
            http_response_code(405);
            echo json_encode(['error' => 'Método no permitido']);
            exit;
    }
    
    http_response_code($result['status']);
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

