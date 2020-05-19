package dev.sejtam.Banka.Utils;

import dev.sejtam.Banka.Banka;
import org.bukkit.scheduler.BukkitRunnable;

public class RunnableHelper {
    private static boolean isStopping;

    public static void setServerStopping() {
        isStopping = true;
    }

    public static void runTask(Runnable runnable) {
        if(isStopping) {
            runnable.run();
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        }.runTask(Banka.getInstance());
    }

    public static void runTaskAsynchronously(Runnable runnable) {
        if(isStopping) {
            runnable.run();
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        }.runTaskAsynchronously(Banka.getInstance());
    }

    public static void runTaskLater(Runnable runnable, long delay) {
        if(isStopping) {
            runnable.run();
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        }.runTaskLater(Banka.getInstance(), delay);
    }

    public static void runTaskLater(Runnable runnable) {
        runTaskLater(runnable, 1);
    }

    public static void runTaskLaterAsynchronously(Runnable runnable, long delay) {
        if(isStopping) {
            runnable.run();
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        }.runTaskLaterAsynchronously(Banka.getInstance(), delay);
    }

    public static void runTaskLaterAsynchronously(Runnable runnable) {
        runTaskLaterAsynchronously(runnable, 1);
    }

    public static void runTaskTimer(Runnable runnable, long delay, long period) {
        if(isStopping) {
            runnable.run();
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        }.runTaskTimer(Banka.getInstance(), (period <= 20 * 10) ? (delay + RunnableDelay.getDelay()) : delay, period);
    }

    public static void runTaskTimer(Runnable runnable, long period) {
        runTaskTimer(runnable, 0, period);
    }

    public static void runTaskTimerAsynchronously(Runnable runnable, long delay, long period) {
        if(isStopping) {
            runnable.run();
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        }.runTaskTimerAsynchronously(Banka.getInstance(), (period <= 20 * 10) ? (delay + RunnableDelay.getDelay()) : delay, period);
    }

    public static void runTaskTimerAsynchronously(Runnable runnable, long period) {
        runTaskTimerAsynchronously(runnable, 0, period);
    }

    public static class RunnableDelay {

        private static int[] delays = new int[] {
                0, 10, 5, 15,
                2, 12, 7, 17,
                4, 14, 9, 19,
                1, 11, 6, 16,
                3, 13, 8, 18
        };

        private static int delaysCounter = 0;

        public static int getDelay() {
            return delays[(delaysCounter++) % delays.length];
        }
    }
}
