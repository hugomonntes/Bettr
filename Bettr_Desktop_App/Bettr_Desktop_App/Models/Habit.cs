using System;

namespace Bettr_Desktop_App.Models
{
    public class Habit
    {
        public int Id { get; set; }
        public int User_id { get; set; }
        public string Description { get; set; }
        public string Image_url { get; set; }
        public string Habit_type { get; set; }
        public int Likes_count { get; set; }
        public string Username { get; set; }
        public string Name { get; set; }
        public DateTime Created_at { get; set; }
        public DateTime Updated_at { get; set; }
    }
}
