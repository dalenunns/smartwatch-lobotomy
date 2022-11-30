public class MyLogger {
    public static boolean ENABLE = true;

    public static void d(String str) {
        if (ENABLE) {
            System.out.println(ConsoleColors.YELLOW + "DEBUG:" + ConsoleColors.RESET + " "+str);
        }
    }

    public static void d(String str, Throwable th) {
        if (ENABLE) {
            System.out.println(ConsoleColors.YELLOW + "DEBUG:" + ConsoleColors.RESET + " "+str + " Exception:");
            th.printStackTrace();
        }
    }

}
