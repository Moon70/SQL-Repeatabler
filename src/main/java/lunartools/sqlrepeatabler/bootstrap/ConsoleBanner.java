package lunartools.sqlrepeatabler.bootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsoleBanner {
    private static Logger logger = LoggerFactory.getLogger(ConsoleBanner.class);

    public static void logCuteLittleAnsiApe() {
        logger.info(replaceSpaceWithNonBreakingSpace("               __,__"));
        logger.info(replaceSpaceWithNonBreakingSpace("      .--.  .-\"     \"-.  .--."));
        logger.info(replaceSpaceWithNonBreakingSpace("     / .. \\/  .-. .-.  \\/ .. \\"));
        logger.info(replaceSpaceWithNonBreakingSpace("    | |  '|  /   Y   \\  |'  | |"));
        logger.info(replaceSpaceWithNonBreakingSpace("    | \\   \\  \\ 0 | 0 /  /   / |"));
        logger.info(replaceSpaceWithNonBreakingSpace("     \\ '- ,\\.-\"\"\"\"\"\"\"-./, -' /"));
        logger.info(replaceSpaceWithNonBreakingSpace("      ''-' /_   ^ ^   _\\ '-''"));
        logger.info(replaceSpaceWithNonBreakingSpace("          |  \\._   _./  |"));
        logger.info(replaceSpaceWithNonBreakingSpace("          \\   \\ `~` /   /"));
        logger.info(replaceSpaceWithNonBreakingSpace("           '._ '-=-' _.'"));
        logger.info(replaceSpaceWithNonBreakingSpace("              '-----'"));
    }

    private static String replaceSpaceWithNonBreakingSpace(String s) {
        return s.replace(' ', (char)160);
    }

}
