package com.example.kcs.Fragment.PlaceOrders.Session;

public class SelectedSessionList  {
        private final String session_title,date_time;
        public SelectedSessionList(String session_title,String date_time) {
            this.session_title = session_title;
            this.date_time = date_time;
        }

        public String getSession_title() {
            return session_title;
        }

    public String getDate_time() {
        return date_time;
    }
}
