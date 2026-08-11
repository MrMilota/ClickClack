package ru.broncks.plugins.clickclack.handler;

import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import ru.broncks.plugins.clickclack.config.ConfigManager;
import net.minecraft.client.Minecraft;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ClickHandler {
    private static boolean isRunning = false;

    public static void onLeftClick() {
        if (isRunning) return;
        int count = ConfigManager.getConfig().general.clicksPerPress;
        int delay = ConfigManager.getConfig().general.delayLeftBetweenClicks;
        sendLeftClicks(count, delay);
    }

    public static void onRemappedRightClick() {
        if (isRunning) return;
        int count = ConfigManager.getConfig().general.rightClicksPerPress;
        int delay = ConfigManager.getConfig().general.delayRightBetweenClicks;
        sendLeftClicks(count, delay);
    }

    private static void sendLeftClicks(int count, int delay) {
        if (count <= 0) return;
        isRunning = true;

        new Thread(() -> {
            try {
            Minecraft client = Minecraft.getInstance();
            if (client.player == null || client.gameMode == null) {
                return;
            }

            for (int i = 0; i < count; i++) {
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }

                client.execute(() -> sendLeftClick(client, client.gameMode));
                ClickCounter.registerLeftClick();

                if (i < count - 1 && delay > 0) {
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }

        } finally {
            isRunning = false;
        }
        }).start();
    }

    private static void sendLeftClick(Minecraft client, MultiPlayerGameMode gameMode) {
        HitResult hit = client.hitResult;

        if (hit == null) return;

        if (hit.getType() == HitResult.Type.ENTITY) {
            Entity target = ((EntityHitResult) hit).getEntity();
            gameMode.attack(client.player, target);
        } else if (hit.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHit = (BlockHitResult) hit;
            BlockPos pos = blockHit.getBlockPos();
            Direction side = blockHit.getDirection();
            gameMode.startDestroyBlock(pos, side);
        }

        client.player.swing(InteractionHand.MAIN_HAND);
    }
}