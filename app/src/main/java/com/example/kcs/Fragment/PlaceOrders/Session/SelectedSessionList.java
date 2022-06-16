package com.example.kcs.Fragment.PlaceOrders.Session;

public class SelectedSessionList  {
        private  String session_title,date_time,bolen;
        public SelectedSessionList() {

        }

    public void setSession_title(String session_title) {
        this.session_title = session_title;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public void setBolen(String bolen) {
        this.bolen = bolen;
    }

    public String getBolen() {
        return bolen;
    }

    public String getSession_title() {
            return session_title;
        }

    public String getDate_time() {
        return date_time;
    }
}
