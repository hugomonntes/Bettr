using System;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;
using System.Web.Script.Serialization;
using Bettr_Desktop_App.Models;

namespace Bettr_Desktop_App.Api
{
    public class ApiService
    {
        private static readonly HttpClient client = new HttpClient();
        private const string BaseUrl = "https://bettr-g5yv.onrender.com/rest";

        public async Task<User> LoginAsync(string username, string passwordHash)
        {
            try
            {
                // Codificamos para evitar errores con caracteres especiales en la URL
                string safeUser = Uri.EscapeDataString(username);
                string safePass = Uri.EscapeDataString(passwordHash);

                string url = $"{BaseUrl}/users/{safeUser}/{safePass}";
                
                HttpResponseMessage response = await client.GetAsync(url);
                
                if (response.IsSuccessStatusCode)
                {
                    string jsonResponse = await response.Content.ReadAsStringAsync();
                    JavaScriptSerializer serializer = new JavaScriptSerializer();
                    return serializer.Deserialize<User>(jsonResponse);
                }
                
                return null;
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error during login: {ex.Message}");
                return null;
            }
        }

        public async Task<bool> RegisterAsync(User user)
        {
            try
            {
                string url = $"{BaseUrl}/users";
                JavaScriptSerializer serializer = new JavaScriptSerializer();
                string jsonBody = serializer.Serialize(user);
                
                StringContent content = new StringContent(jsonBody, Encoding.UTF8, "application/json");
                
                HttpResponseMessage response = await client.PostAsync(url, content);
                
                return response.StatusCode == System.Net.HttpStatusCode.Created;
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error during registration: {ex.Message}");
                return false;
            }
        }
    }
}
