package nz.ac.auckland.se206;

public class Utils {
  public static String appendBeforeExtension(String url, String append) {
    int lastDot = url.lastIndexOf(".");
    return url.substring(0, lastDot) + append + url.substring(lastDot);
  }
}
