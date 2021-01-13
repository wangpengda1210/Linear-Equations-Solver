class Time {

    private int hours;
    private int minutes;
    private int seconds;

    public Time(int hours, int minutes, int seconds) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    @Override
    public String toString() {
        return addZero(hours) + ":" + addZero(minutes) + ":" + addZero(seconds);
    }

    private String addZero(int digit) {
        String result = String.valueOf(digit);

        if (result.length() == 1) {
            result = "0" + result;
        }

        return result;
    }

}