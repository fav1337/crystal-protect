package com.crystalprotect.screen;

import com.crystalprotect.config.CrystalProtectConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

@Environment(EnvType.CLIENT)
public class CrystalProtectConfigScreen extends Screen {

    private final Screen parent;
    private final CrystalProtectConfig.ConfigData cfg;

    // Slider cho thời gian
    private ProtectionSlider slider;

    public CrystalProtectConfigScreen(Screen parent) {
        super(Text.literal("§6⚔ Crystal Kill Protect §7- Cài đặt"));
        this.parent = parent;
        this.cfg = CrystalProtectConfig.get();
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int startY = 70;
        int btnWidth = 280;
        int btnHeight = 20;
        int gap = 26;

        // Slider - Thời gian bảo vệ
        slider = new ProtectionSlider(
            centerX - btnWidth / 2, startY,
            btnWidth, btnHeight,
            cfg.protectionSeconds
        );
        this.addDrawableChild(slider);

        // Toggle - Hiện thông báo
        addToggleButton(centerX, startY + gap,
            btnWidth, btnHeight,
            "Hiển thị thông báo Action Bar",
            "Bật/tắt thông báo trên màn hình khi bảo vệ kích hoạt",
            cfg.showMessages,
            val -> cfg.showMessages = val);

        // Toggle - Chặn đặt crystal
        addToggleButton(centerX, startY + gap * 2,
            btnWidth, btnHeight,
            "Chặn ĐẶT Crystal",
            "Ngăn không cho đặt End Crystal trong thời gian bảo vệ",
            cfg.blockPlace,
            val -> cfg.blockPlace = val);

        // Toggle - Chặn đập crystal
        addToggleButton(centerX, startY + gap * 3,
            btnWidth, btnHeight,
            "Chặn ĐẬP Crystal",
            "Ngăn không cho tấn công End Crystal trong thời gian bảo vệ",
            cfg.blockAttack,
            val -> cfg.blockAttack = val);

        // Toggle - Chặn tương tác
        addToggleButton(centerX, startY + gap * 4,
            btnWidth, btnHeight,
            "Chặn TƯƠNG TÁC Crystal",
            "Ngăn click chuột phải vào End Crystal",
            cfg.blockInteract,
            val -> cfg.blockInteract = val);

        // Toggle - Bảo vệ khi kill bằng crystal
        addToggleButton(centerX, startY + gap * 5,
            btnWidth, btnHeight,
            "Bảo vệ khi kill gián tiếp (Crystal Kill)",
            "Kích hoạt bảo vệ kể cả khi player chết do crystal của bạn",
            cfg.protectOnCrystalKill,
            val -> cfg.protectOnCrystalKill = val);

        // Nút Lưu
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("§a✔ Lưu và Đóng"),
            btn -> {
                CrystalProtectConfig.save();
                this.client.setScreen(parent);
            })
            .dimensions(centerX - 130, this.height - 40, 120, 20)
            .build()
        );

        // Nút Hủy
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("§c✖ Hủy"),
            btn -> this.client.setScreen(parent))
            .dimensions(centerX + 10, this.height - 40, 120, 20)
            .build()
        );

        // Nút Reset
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("§e↺ Reset mặc định"),
            btn -> resetDefaults())
            .dimensions(centerX - 60, this.height - 65, 120, 20)
            .tooltip(Tooltip.of(Text.literal("Đặt lại tất cả về mặc định")))
            .build()
        );
    }

    private void addToggleButton(int centerX, int y, int width, int height,
                                  String label, String tooltip, boolean initial,
                                  java.util.function.Consumer<Boolean> setter) {
        boolean[] state = {initial};
        ButtonWidget[] btnRef = new ButtonWidget[1];

        ButtonWidget btn = ButtonWidget.builder(
            getToggleText(label, state[0]),
            b -> {
                state[0] = !state[0];
                setter.accept(state[0]);
                b.setMessage(getToggleText(label, state[0]));
            })
            .dimensions(centerX - width / 2, y, width, height)
            .tooltip(Tooltip.of(Text.literal(tooltip)))
            .build();

        btnRef[0] = btn;
        this.addDrawableChild(btn);
    }

    private Text getToggleText(String label, boolean val) {
        String status = val ? "§a[BẬT]" : "§c[TẮT]";
        return Text.literal(status + " §f" + label);
    }

    private void resetDefaults() {
        cfg.protectionSeconds = 3.0f;
        cfg.showMessages = true;
        cfg.blockPlace = true;
        cfg.blockAttack = true;
        cfg.blockInteract = true;
        cfg.protectOnCrystalKill = true;

        // Rebuild screen
        this.clearChildren();
        this.init();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Nền mờ
        this.renderBackground(context, mouseX, mouseY, delta);

        // Tiêu đề
        context.drawCenteredTextWithShadow(
            this.textRenderer,
            this.title,
            this.width / 2, 15, 0xFFFFFF
        );

        // Phụ đề
        context.drawCenteredTextWithShadow(
            this.textRenderer,
            Text.literal("§7Cài đặt cho Crystal PvP - tránh tự nổ sau khi kill"),
            this.width / 2, 28, 0xAAAAAA
        );

        // Label cho slider
        context.drawCenteredTextWithShadow(
            this.textRenderer,
            Text.literal("§eThời gian bảo vệ sau khi kill:"),
            this.width / 2, 57, 0xFFDD44
        );

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        this.client.setScreen(parent);
    }

    // ---- Slider Widget ----
    private class ProtectionSlider extends SliderWidget {
        // Min 0.5s, Max 10s
        private static final float MIN = 0.5f;
        private static final float MAX = 10.0f;

        public ProtectionSlider(int x, int y, int width, int height, float value) {
            super(x, y, width, height,
                Text.literal(""), // placeholder
                (value - MIN) / (MAX - MIN));
            updateMessage();
        }

        @Override
        protected void updateMessage() {
            float val = MIN + (float) this.value * (MAX - MIN);
            val = Math.round(val * 2) / 2.0f; // bước 0.5
            cfg.protectionSeconds = val;
            setMessage(Text.literal("§e⏱ Thời gian: §f" + val + " giây"));
        }

        @Override
        protected void applyValue() {
            float val = MIN + (float) this.value * (MAX - MIN);
            val = Math.round(val * 2) / 2.0f;
            cfg.protectionSeconds = val;
        }
    }
}
