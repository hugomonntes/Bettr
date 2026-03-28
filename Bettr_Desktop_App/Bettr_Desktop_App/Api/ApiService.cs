using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;
using Bettr_Desktop_App.Models;

namespace Bettr_Desktop_App.Api
{
    public class ApiService
    {
        private static readonly HttpClient client = new HttpClient();
        private const string BaseUrl = "https://bettr-g5yv.onrender.com/rest";

        static ApiService()
        {
            client.Timeout = TimeSpan.FromSeconds(30);
        }

        public static User CurrentUser { get; set; }

        private string HashPassword(string password)
        {
            return BCrypt.Net.BCrypt.HashPassword(password, BCrypt.Net.BCrypt.GenerateSalt(10));
        }

        private bool VerifyPassword(string password, string hashedPassword)
        {
            return BCrypt.Net.BCrypt.Verify(password, hashedPassword);
        }

        private string SerializeObject(object obj)
        {
            var type = obj.GetType();
            if (obj is Dictionary<string, string> dictStr)
            {
                var sb = new StringBuilder("{");
                int i = 0;
                foreach (var kvp in dictStr)
                {
                    if (i > 0) sb.Append(",");
                    sb.Append($"\"{kvp.Key}\":\"{EscapeJsonString(kvp.Value)}\"");
                    i++;
                }
                sb.Append("}");
                return sb.ToString();
            }
            else if (obj is Dictionary<string, object> dictObj)
            {
                var sb = new StringBuilder("{");
                int i = 0;
                foreach (var kvp in dictObj)
                {
                    if (i > 0) sb.Append(",");
                    if (kvp.Value is string strVal)
                        sb.Append($"\"{kvp.Key}\":\"{EscapeJsonString(strVal)}\"");
                    else if (kvp.Value is int intVal)
                        sb.Append($"\"{kvp.Key}\":{intVal}");
                    else if (kvp.Value == null)
                        sb.Append($"\"{kvp.Key}\":null");
                    else
                        sb.Append($"\"{kvp.Key}\":\"{EscapeJsonString(kvp.Value.ToString())}\"");
                    i++;
                }
                sb.Append("}");
                return sb.ToString();
            }
            return "{}";
        }

        private string EscapeJsonString(string str)
        {
            if (string.IsNullOrEmpty(str)) return "";
            return str.Replace("\\", "\\\\").Replace("\"", "\\\"").Replace("\n", "\\n").Replace("\r", "\\r").Replace("\t", "\\t");
        }

        private T DeserializeJson<T>(string json) where T : class
        {
            try
            {
                if (json.Trim().StartsWith("["))
                {
                    json = "{\"items\":" + json + "}";
                    var wrapper = ParseJsonToDictionary(json);
                    if (wrapper != null && wrapper.ContainsKey("items"))
                    {
                        var items = wrapper["items"];
                        if (items is List<object> list)
                        {
                            if (typeof(T) == typeof(List<User>))
                                return ConvertToUserList(list) as T;
                            if (typeof(T) == typeof(List<Habit>))
                                return ConvertToHabitList(list) as T;
                        }
                    }
                    return null;
                }
                else
                {
                    var dict = ParseJsonToDictionary(json);
                    if (dict == null) return null;
                    if (typeof(T) == typeof(User))
                        return ConvertToUser(dict) as T;
                    if (typeof(T) == typeof(Habit))
                        return ConvertToHabit(dict) as T;
                    if (typeof(T) == typeof(Dictionary<string, bool>))
                        return new Dictionary<string, bool> { { "result", dict.ContainsKey("liked") ? (bool)dict["liked"] : dict.ContainsKey("following") && (bool)dict["following"] } } as T;
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Deserialization error: {ex.Message}");
            }
            return null;
        }

        private Dictionary<string, object> ParseJsonToDictionary(string json)
        {
            var dict = new Dictionary<string, object>();
            json = json.Trim();
            if (json.StartsWith("{") && json.EndsWith("}"))
            {
                json = json.Substring(1, json.Length - 2);
                int braceCount = 0;
                int bracketCount = 0;
                int start = 0;
                bool inString = false;

                for (int i = 0; i < json.Length; i++)
                {
                    if (json[i] == '"' && (i == 0 || json[i - 1] != '\\'))
                        inString = !inString;
                    if (!inString)
                    {
                        if (json[i] == '{') braceCount++;
                        else if (json[i] == '}') braceCount--;
                        else if (json[i] == '[') bracketCount++;
                        else if (json[i] == ']') bracketCount--;
                        else if (json[i] == ',' && braceCount == 0 && bracketCount == 0)
                        {
                            ParseKeyValue(json.Substring(start, i - start), dict);
                            start = i + 1;
                        }
                    }
                }
                if (start < json.Length)
                    ParseKeyValue(json.Substring(start), dict);
            }
            return dict;
        }

        private void ParseKeyValue(string pair, Dictionary<string, object> dict)
        {
            pair = pair.Trim();
            int colonIndex = -1;
            bool inString = false;

            for (int i = 0; i < pair.Length; i++)
            {
                if (pair[i] == '"' && (i == 0 || pair[i - 1] != '\\'))
                    inString = !inString;
                if (!inString && pair[i] == ':')
                {
                    colonIndex = i;
                    break;
                }
            }

            if (colonIndex < 0) return;

            string key = pair.Substring(0, colonIndex).Trim();
            if (key.StartsWith("\"")) key = key.Substring(1);
            if (key.EndsWith("\"")) key = key.Substring(0, key.Length - 1);

            string value = pair.Substring(colonIndex + 1).Trim();

            if (value == "null")
                dict[key] = null;
            else if (value.StartsWith("\"") && value.EndsWith("\""))
                dict[key] = value.Substring(1, value.Length - 2).Replace("\\\"", "\"").Replace("\\\\", "\\");
            else if (value == "true")
                dict[key] = true;
            else if (value == "false")
                dict[key] = false;
            else if (int.TryParse(value, out int intVal))
                dict[key] = intVal;
            else if (double.TryParse(value, out double dblVal))
                dict[key] = dblVal;
            else if (value.StartsWith("[") && value.EndsWith("]"))
                dict[key] = ParseJsonArray(value);
            else
                dict[key] = value;
        }

        private List<object> ParseJsonArray(string json)
        {
            var list = new List<object>();
            json = json.Trim();
            if (json.StartsWith("[") && json.EndsWith("]"))
            {
                json = json.Substring(1, json.Length - 2);
                int braceCount = 0;
                int bracketCount = 0;
                int start = 0;
                bool inString = false;

                for (int i = 0; i < json.Length; i++)
                {
                    if (json[i] == '"' && (i == 0 || json[i - 1] != '\\'))
                        inString = !inString;
                    if (!inString)
                    {
                        if (json[i] == '{') braceCount++;
                        else if (json[i] == '}') braceCount--;
                        else if (json[i] == '[') bracketCount++;
                        else if (json[i] == ']') bracketCount--;
                        else if (json[i] == ',' && braceCount == 0 && bracketCount == 0)
                        {
                            list.Add(ParseJsonValue(json.Substring(start, i - start).Trim()));
                            start = i + 1;
                        }
                    }
                }
                if (start < json.Length)
                    list.Add(ParseJsonValue(json.Substring(start).Trim()));
            }
            return list;
        }

        private object ParseJsonValue(string value)
        {
            value = value.Trim();
            if (value == "null")
                return null;
            if (value.StartsWith("{") && value.EndsWith("}"))
                return ParseJsonToDictionary(value);
            if (value.StartsWith("[") && value.EndsWith("]"))
                return ParseJsonArray(value);
            if (value.StartsWith("\"") && value.EndsWith("\""))
                return value.Substring(1, value.Length - 2).Replace("\\\"", "\"").Replace("\\\\", "\\");
            if (value == "true")
                return true;
            if (value == "false")
                return false;
            if (int.TryParse(value, out int intVal))
                return intVal;
            if (double.TryParse(value, out double dblVal))
                return dblVal;
            return value;
        }

        private List<User> ConvertToUserList(List<object> items)
        {
            var users = new List<User>();
            foreach (var item in items)
            {
                if (item is Dictionary<string, object> dict)
                    users.Add(ConvertToUser(dict));
            }
            return users;
        }

        private List<Habit> ConvertToHabitList(List<object> items)
        {
            var habits = new List<Habit>();
            foreach (var item in items)
            {
                if (item is Dictionary<string, object> dict)
                    habits.Add(ConvertToHabit(dict));
            }
            return habits;
        }

        private User ConvertToUser(Dictionary<string, object> dict)
        {
            var user = new User();
            if (dict.ContainsKey("id")) user.Id = Convert.ToInt32(dict["id"]);
            if (dict.ContainsKey("name")) user.Name = dict["name"]?.ToString();
            if (dict.ContainsKey("username")) user.Username = dict["username"]?.ToString();
            if (dict.ContainsKey("email")) user.Email = dict["email"]?.ToString();
            if (dict.ContainsKey("description")) user.Description = dict["description"]?.ToString();
            if (dict.ContainsKey("avatar")) user.Avatar = dict["avatar"]?.ToString();
            if (dict.ContainsKey("password_hash")) user.Password_hash = dict["password_hash"]?.ToString();
            if (dict.ContainsKey("created_at") && dict["created_at"] != null)
                user.Created_at = DateTime.TryParse(dict["created_at"].ToString(), out var dt) ? dt : DateTime.Now;
            return user;
        }

        private Habit ConvertToHabit(Dictionary<string, object> dict)
        {
            var habit = new Habit();
            if (dict.ContainsKey("id")) habit.Id = Convert.ToInt32(dict["id"]);
            if (dict.ContainsKey("user_id")) habit.User_id = Convert.ToInt32(dict["user_id"]);
            if (dict.ContainsKey("description")) habit.Description = dict["description"]?.ToString();
            if (dict.ContainsKey("image_url")) habit.Image_url = dict["image_url"]?.ToString();
            if (dict.ContainsKey("habit_type")) habit.Habit_type = dict["habit_type"]?.ToString();
            if (dict.ContainsKey("likes_count")) habit.Likes_count = Convert.ToInt32(dict["likes_count"]);
            if (dict.ContainsKey("username")) habit.Username = dict["username"]?.ToString();
            if (dict.ContainsKey("name")) habit.Name = dict["name"]?.ToString();
            if (dict.ContainsKey("created_at") && dict["created_at"] != null)
                habit.Created_at = DateTime.TryParse(dict["created_at"].ToString(), out var dt) ? dt : DateTime.Now;
            return habit;
        }

        public async Task<User> LoginAsync(string username, string password)
        {
            try
            {
                string safeUser = Uri.EscapeDataString(username);
                string url = $"{BaseUrl}/users/username/{safeUser}";

                HttpResponseMessage response = await client.GetAsync(url);

                string responseText = await response.Content.ReadAsStringAsync();
                Console.WriteLine($"Login Response: {response.StatusCode} - {responseText}");

                if (response.StatusCode == System.Net.HttpStatusCode.OK || response.StatusCode == System.Net.HttpStatusCode.Accepted)
                {
                    var dict = ParseJsonToDictionary(responseText);
                    if (dict != null)
                    {
                        string storedHash = null;
                        if (dict.ContainsKey("password_hash"))
                            storedHash = dict["password_hash"]?.ToString();
                        else if (dict.ContainsKey("passwordHash"))
                            storedHash = dict["passwordHash"]?.ToString();

                        if (storedHash != null && VerifyPassword(password, storedHash))
                        {
                            var user = ConvertToUser(dict);
                            CurrentUser = user;
                            return user;
                        }
                    }
                }
                else if (response.StatusCode == System.Net.HttpStatusCode.Unauthorized)
                {
                    return null;
                }
                else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                {
                    return null;
                }

                return null;
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Login error: {ex.Message}");
                throw;
            }
        }

        public async Task<User> GetUserByIdAsync(int userId)
        {
            try
            {
                string url = $"{BaseUrl}/users/{userId}";
                HttpResponseMessage response = await client.GetAsync(url);

                if (response.IsSuccessStatusCode)
                {
                    string jsonResponse = await response.Content.ReadAsStringAsync();
                    return DeserializeJson<User>(jsonResponse);
                }

                return null;
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error getting user: {ex.Message}");
                return null;
            }
        }

        public async Task<bool> RegisterAsync(string name, string username, string email, string password)
        {
            try
            {
                string passwordHash = HashPassword(password);

                var json = $"{{\"name\":\"{EscapeJsonString(name)}\",\"username\":\"{EscapeJsonString(username)}\",\"email\":\"{EscapeJsonString(email)}\",\"password_hash\":\"{passwordHash}\"}}";

                string url = $"{BaseUrl}/users";
                StringContent content = new StringContent(json, Encoding.UTF8, "application/json");

                HttpResponseMessage response = await client.PostAsync(url, content);

                string responseText = await response.Content.ReadAsStringAsync();
                Console.WriteLine($"Register Response: {response.StatusCode} - {responseText}");

                return response.StatusCode == System.Net.HttpStatusCode.Created ||
                       response.StatusCode == System.Net.HttpStatusCode.OK ||
                       response.StatusCode == System.Net.HttpStatusCode.Created;
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Register error: {ex.Message}");
                throw;
            }
        }

        public async Task<bool> UpdateProfileAsync(int userId, string description, string avatar)
        {
            try
            {
                var json = $"{{\"description\":\"{EscapeJsonString(description)}\",\"avatar\":\"{EscapeJsonString(avatar)}\"}}";

                string url = $"{BaseUrl}/users/{userId}/profile";
                StringContent content = new StringContent(json, Encoding.UTF8, "application/json");

                HttpResponseMessage response = await client.PostAsync(url, content);

                return response.IsSuccessStatusCode;
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error updating profile: {ex.Message}");
                throw;
            }
        }

        public async Task<List<Habit>> GetFeedAsync(int userId)
        {
            try
            {
                string url = $"{BaseUrl}/habits/feed/{userId}";
                HttpResponseMessage response = await client.GetAsync(url);

                if (response.IsSuccessStatusCode)
                {
                    string jsonResponse = await response.Content.ReadAsStringAsync();
                    return DeserializeJson<List<Habit>>(jsonResponse) ?? new List<Habit>();
                }

                return new List<Habit>();
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error getting feed: {ex.Message}");
                return new List<Habit>();
            }
        }

        public async Task<List<Habit>> GetUserHabitsAsync(int userId)
        {
            try
            {
                string url = $"{BaseUrl}/habits/user/{userId}";
                HttpResponseMessage response = await client.GetAsync(url);

                if (response.IsSuccessStatusCode)
                {
                    string jsonResponse = await response.Content.ReadAsStringAsync();
                    return DeserializeJson<List<Habit>>(jsonResponse) ?? new List<Habit>();
                }

                return new List<Habit>();
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error getting user habits: {ex.Message}");
                return new List<Habit>();
            }
        }

        public async Task<bool> CreateHabitAsync(int userId, string description, string imageUrl, string habitType)
        {
            try
            {
                var json = $"{{\"user_id\":{userId},\"description\":\"{EscapeJsonString(description)}\",\"image_url\":\"{EscapeJsonString(imageUrl)}\",\"habit_type\":\"{EscapeJsonString(habitType)}\"}}";

                string url = $"{BaseUrl}/habits";
                StringContent content = new StringContent(json, Encoding.UTF8, "application/json");

                HttpResponseMessage response = await client.PostAsync(url, content);

                return response.IsSuccessStatusCode;
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error creating habit: {ex.Message}");
                throw;
            }
        }

        public async Task<bool> IsLikedAsync(int habitId, int userId)
        {
            try
            {
                string url = $"{BaseUrl}/habits/{habitId}/isliked/{userId}";
                HttpResponseMessage response = await client.GetAsync(url);

                if (response.IsSuccessStatusCode)
                {
                    string jsonResponse = await response.Content.ReadAsStringAsync();
                    var dict = ParseJsonToDictionary(jsonResponse);
                    return dict != null && dict.ContainsKey("liked") && (bool)dict["liked"];
                }

                return false;
            }
            catch
            {
                return false;
            }
        }

        public async Task<bool> LikeHabitAsync(int habitId, int userId)
        {
            try
            {
                string url = $"{BaseUrl}/habits/{habitId}/like/{userId}";
                StringContent content = new StringContent("{}", Encoding.UTF8, "application/json");

                HttpResponseMessage response = await client.PostAsync(url, content);
                return response.IsSuccessStatusCode;
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error liking habit: {ex.Message}");
                return false;
            }
        }

        public async Task<bool> UnlikeHabitAsync(int habitId, int userId)
        {
            try
            {
                string url = $"{BaseUrl}/habits/{habitId}/like/{userId}";
                var request = new HttpRequestMessage(HttpMethod.Delete, url);

                HttpResponseMessage response = await client.SendAsync(request);
                return response.IsSuccessStatusCode;
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error unliking habit: {ex.Message}");
                return false;
            }
        }

        public async Task<List<User>> SearchUsersAsync(string query = "")
        {
            try
            {
                string url = $"{BaseUrl}/users";
                if (!string.IsNullOrEmpty(query))
                {
                    url += $"?search={Uri.EscapeDataString(query)}";
                }

                HttpResponseMessage response = await client.GetAsync(url);

                if (response.IsSuccessStatusCode)
                {
                    string jsonResponse = await response.Content.ReadAsStringAsync();
                    return DeserializeJson<List<User>>(jsonResponse) ?? new List<User>();
                }

                return new List<User>();
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error searching users: {ex.Message}");
                return new List<User>();
            }
        }

        public async Task<bool> IsFollowingAsync(int followerId, int followingId)
        {
            try
            {
                string url = $"{BaseUrl}/users/isfollowing/{followerId}/{followingId}";
                HttpResponseMessage response = await client.GetAsync(url);

                if (response.IsSuccessStatusCode)
                {
                    string jsonResponse = await response.Content.ReadAsStringAsync();
                    var dict = ParseJsonToDictionary(jsonResponse);
                    return dict != null && dict.ContainsKey("following") && (bool)dict["following"];
                }

                return false;
            }
            catch
            {
                return false;
            }
        }

        public async Task<bool> FollowUserAsync(int followerId, int followingId)
        {
            try
            {
                string url = $"{BaseUrl}/users/follow/{followerId}/{followingId}";
                StringContent content = new StringContent("{}", Encoding.UTF8, "application/json");

                HttpResponseMessage response = await client.PostAsync(url, content);
                return response.IsSuccessStatusCode;
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error following user: {ex.Message}");
                return false;
            }
        }

        public async Task<bool> UnfollowUserAsync(int followerId, int followingId)
        {
            try
            {
                string url = $"{BaseUrl}/users/unfollow/{followerId}/{followingId}";
                var request = new HttpRequestMessage(HttpMethod.Delete, url);

                HttpResponseMessage response = await client.SendAsync(request);
                return response.IsSuccessStatusCode;
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error unfollowing user: {ex.Message}");
                return false;
            }
        }

        public async Task<Dictionary<string, int>> GetUserStatsAsync(int userId)
        {
            try
            {
                string url = $"{BaseUrl}/users/{userId}/stats";
                HttpResponseMessage response = await client.GetAsync(url);

                if (response.IsSuccessStatusCode)
                {
                    string jsonResponse = await response.Content.ReadAsStringAsync();
                    var dict = ParseJsonToDictionary(jsonResponse);
                    var stats = new Dictionary<string, int>();
                    if (dict != null)
                    {
                        if (dict.ContainsKey("followers")) stats["followers"] = Convert.ToInt32(dict["followers"]);
                        if (dict.ContainsKey("following")) stats["following"] = Convert.ToInt32(dict["following"]);
                        if (dict.ContainsKey("habits")) stats["habits"] = Convert.ToInt32(dict["habits"]);
                    }
                    return stats;
                }

                return new Dictionary<string, int> { { "followers", 0 }, { "following", 0 }, { "habits", 0 } };
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error getting stats: {ex.Message}");
                return new Dictionary<string, int> { { "followers", 0 }, { "following", 0 }, { "habits", 0 } };
            }
        }

        public static string GetTimeAgo(DateTime date)
        {
            if (date == default(DateTime))
                return "Ahora";

            TimeSpan diff = DateTime.Now - date;

            if (diff.TotalSeconds < 60)
                return "Ahora";
            if (diff.TotalMinutes < 60)
                return $"{(int)diff.TotalMinutes}m";
            if (diff.TotalHours < 24)
                return $"{(int)diff.TotalHours}h";
            if (diff.TotalDays < 7)
                return $"{(int)diff.TotalDays}d";
            return $"{(int)(diff.TotalDays / 7)}sem";
        }

        public async Task<List<User>> GetFollowersAsync(int userId)
        {
            try
            {
                string url = $"{BaseUrl}/users/{userId}/followers";
                HttpResponseMessage response = await client.GetAsync(url);

                if (response.IsSuccessStatusCode)
                {
                    string jsonResponse = await response.Content.ReadAsStringAsync();
                    return DeserializeJson<List<User>>(jsonResponse) ?? new List<User>();
                }

                return new List<User>();
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error getting followers: {ex.Message}");
                return new List<User>();
            }
        }

        public async Task<List<User>> GetFollowingAsync(int userId)
        {
            try
            {
                string url = $"{BaseUrl}/users/{userId}/following";
                HttpResponseMessage response = await client.GetAsync(url);

                if (response.IsSuccessStatusCode)
                {
                    string jsonResponse = await response.Content.ReadAsStringAsync();
                    return DeserializeJson<List<User>>(jsonResponse) ?? new List<User>();
                }

                return new List<User>();
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error getting following: {ex.Message}");
                return new List<User>();
            }
        }

        public async Task<bool> UpdateUserDescriptionAsync(int userId, string description)
        {
            try
            {
                var json = $"{{\"description\":\"{EscapeJsonString(description)}\"}}";
                string url = $"{BaseUrl}/users/{userId}/description";
                StringContent content = new StringContent(json, Encoding.UTF8, "application/json");

                HttpResponseMessage response = await client.PostAsync(url, content);
                return response.IsSuccessStatusCode;
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error updating description: {ex.Message}");
                return false;
            }
        }
    }
}
