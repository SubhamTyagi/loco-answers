package ai.loko.hk.ui.utils.functions;

import java.util.concurrent.ConcurrentMap;

/**
 * Similar to {@link java.util.function.Consumer}
 * Functional Interface
 * accepts five String and one String Array
 */
@FunctionalInterface
public interface Consumer4{
    /**
     * @param ans               Result sent by Engine.class
     * @param opt1              Reading per word with option for Option 1
     * @param opt2              Reading per word with option for Option 2
     * @param opt3              Reading per word with option for Option 3
     * @param opt4              Reading per word with option for Option 3
     * @param questionAndOption Actual Question and Option
     */
    void accept(
            String ans,
            String opt1,
            String opt2,
            String opt3,
            String opt4,
            String[] questionAndOption);
}
