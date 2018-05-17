package cn.a4miles.okex_monitor.util;

/**
 * 少数无法归类的辅助工具
 *
 * @author
 * @create 2018-05-15 上午10:40
 **/
public class OKCommonUtil {
    public static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
