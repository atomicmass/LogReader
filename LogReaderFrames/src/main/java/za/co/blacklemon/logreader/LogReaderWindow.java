package za.co.blacklemon.logreader;

public interface LogReaderWindow {
    void error(String error);
    void warn(String warning);
    void severe(String severe);
    void info(String info);
    void fatal(String fatal);
}
