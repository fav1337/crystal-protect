# ⚔ Crystal Kill Protect
**Fabric Mod | Minecraft 1.21.4 | Java 21**

> Tránh tự nổ bằng End Crystal ngay sau khi giết player trong Crystal PvP

---

## ✨ Tính năng

| Tính năng | Mô tả |
|-----------|-------|
| 🛡 Bảo vệ sau kill | Tự động kích hoạt khi bạn giết player |
| 🚫 Chặn đặt Crystal | Không thể đặt End Crystal trong thời gian bảo vệ |
| 🚫 Chặn đập Crystal | Không thể tấn công End Crystal |
| 🚫 Chặn tương tác | Không thể click vào Crystal |
| ⚙ Menu cài đặt | Tích hợp Mod Menu — chỉnh mọi thứ trong game |
| 💾 Config JSON | Lưu tại `.minecraft/config/crystalprotect.json` |

---

## 📋 Yêu cầu

- **Minecraft** 1.21.4
- **Fabric Loader** ≥ 0.16.9
- **Fabric API**
- **Mod Menu** (tùy chọn — để dùng menu cài đặt)
- **Java** 21+

---

## 🔧 Cài đặt

### Cách 1: Build từ source
```bash
# Clone/giải nén project
cd crystal-kill-protect

# Build
./gradlew build          # Linux/Mac
gradlew.bat build        # Windows

# File .jar xuất hiện tại:
# build/libs/crystal-kill-protect-1.1.0.jar
```

### Cách 2: Chạy trực tiếp trong dev
```bash
./gradlew runClient
```

### Sau khi có .jar
1. Sao chép `crystal-kill-protect-1.1.0.jar` vào thư mục `mods/`
2. Đảm bảo đã cài **Fabric API** và **Mod Menu** (nếu muốn dùng menu)
3. Khởi động Minecraft

---

## ⚙ Menu Cài Đặt

Với **Mod Menu** được cài:
1. Vào **Mods** trong menu chính
2. Tìm **Crystal Kill Protect**
3. Nhấn nút **Config / Cài đặt**

### Các tùy chọn:

| Tùy chọn | Mặc định | Mô tả |
|----------|----------|-------|
| Thời gian bảo vệ | `3.0s` | Kéo slider 0.5s → 10s |
| Hiển thị thông báo | BẬT | Action bar khi bảo vệ kích hoạt/hết |
| Chặn đặt Crystal | BẬT | Tắt nếu muốn vẫn đặt được |
| Chặn đập Crystal | BẬT | Tắt nếu muốn vẫn đập được |
| Chặn tương tác | BẬT | Click chuột phải vào crystal |
| Bảo vệ khi kill bằng Crystal | BẬT | Kill gián tiếp qua explosion |

---

## 📁 Cấu trúc Source

```
src/main/java/com/crystalprotect/
├── CrystalProtectMod.java              ← Entry point chính
├── CrystalProtectModMenuIntegration.java ← Tích hợp Mod Menu
├── config/
│   └── CrystalProtectConfig.java       ← Quản lý config JSON
├── screen/
│   └── CrystalProtectConfigScreen.java ← GUI cài đặt
└── mixin/
    ├── ServerPlayerAttackMixin.java    ← Chặn đập crystal
    ├── EndCrystalPlaceMixin.java       ← Chặn đặt crystal
    └── EndCrystalInteractMixin.java    ← Chặn tương tác
```

---

## 🔧 Tùy chỉnh nâng cao (không cần menu)

Sửa file `config/crystalprotect.json`:
```json
{
  "protectionSeconds": 3.0,
  "showMessages": true,
  "blockPlace": true,
  "blockAttack": true,
  "blockInteract": true,
  "protectOnCrystalKill": true
}
```

---

## ❓ Lưu ý

- Mod hoạt động **phía server** — người chơi không cần cài (trừ menu cần client)
- Mod Menu chỉ cần ở **client** để dùng GUI
- Không ảnh hưởng đến crystal của **người chơi khác**
