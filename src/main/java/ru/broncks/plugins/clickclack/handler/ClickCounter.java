package ru.broncks.plugins.clickclack.handler;

import com.google.common.collect.Lists;
import java.util.Queue;

public class ClickCounter {
    private static final Queue<Long> leftClicks = Lists.newLinkedList();
    private static final Queue<Long> rightClicks = Lists.newLinkedList();

    public static void registerLeftClick() {
        leftClicks.add(System.currentTimeMillis() + 1000L);
    }

    public static void registerRightClick() {
        rightClicks.add(System.currentTimeMillis() + 1000L);
    }

    public static int getLeftCps() {
        return getClicksFromQueue(leftClicks);
    }

    public static int getRightCps() {
        return getClicksFromQueue(rightClicks);
    }

    private static int getClicksFromQueue(Queue<Long> queue) {
        long now = System.currentTimeMillis();
        while (!queue.isEmpty() && queue.peek() < now) {
            queue.remove();
        }
        return queue.size();
    }
}