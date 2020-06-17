package ai.loko.hk.ui.utils.functions;

/**
 * Similar to {@link java.util.function.Consumer}
 * Functional Interface
 */
@FunctionalInterface
public interface Consumer3 {
    /**
     * @param ans               Result sent by Engine.class
     * @param opt1              Reading per word with option for Option 1
     * @param opt2              Reading per word with option for Option 2
     * @param opt3              Reading per word with option for Option 3
     * @param questionAndOption Actual Question and Option
     */
    void accept(String ans, String opt1, String opt2, String opt3, String[] questionAndOption);
}
