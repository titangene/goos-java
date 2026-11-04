# goos-java

用 TDD 重刻 GOOS 書中的 Auction Sniper 範例。專案介紹與使用方式見 [README.md](README.md)。

## 執行 Java

Java 的編譯、執行、測試一律透過腳本在 `toolbox` container 內執行，不在 host 直接跑 `javac`/`java`：

- 全部測試：`scripts/test.sh`
- unit 測試：`scripts/test.sh unit`
- E2E 測試：`scripts/test.sh e2e`
- E2E 測試並在螢幕上顯示 Swing UI（除錯用）：`scripts/test.sh e2e --headed`
- Swing app：`scripts/run-app.sh <itemId> [username] [password]`

## Openfire（XMPP）

E2E 測試和 app 都連到 Openfire，`XMPP_HOSTNAME` 寫死 `"localhost"`：

- 不用先準備環境，第一次跑腳本時會自動跑 setup wizard 建立測試帳號
- 環境管理見 [README.md](README.md#3-管理環境)

## 依賴套件

依賴的 jar 直接 commit 進 `lib/`，不用 Maven/Gradle：

- runtime 需要的放 `lib/deploy`
- 只有開發、測試用的放 `lib/develop`

## TDD commit message 格式

照書中章節逐步開發，commit message 用 Conventional Commits。依 commit 的性質選格式：

### 每個 commit 都綠燈（預設）

```
test(<scope>): skeleton - <骨架描述> [<書中出處>]
feat(<scope>): <綠燈描述> [<書中出處>]
refactor(<scope>): <重構描述> [<書中出處>]
```

- `test(<scope>): skeleton - ...`：只用於**無斷言的測試骨架 commit**。寫出完整的測試流程但不含任何斷言，production code 只補為了通過編譯的空定義。這個 commit 是綠燈
- `feat(<scope>): ...`：新增一個斷言，並補上讓它通過的最小 production code。這個 commit 是綠燈
- `refactor(<scope>): ...`：無行為變更的重構。這個 commit 是綠燈
- 只收緊既有斷言、沒有 production code 改動的 commit 用 `test(<scope>): ...`，不加 `red`/`green`/`skeleton` 前綴，並在 body 說明「本步沒有 production code 改動」

### infra commit

Docker、vendor jar、環境腳本、IDE 設定的改動不走上面的格式，依改動性質用 `build`/`feat`/`fix`，並在 body 寫下這個 commit 的**綠燈判準指令**（一條完成前會失敗、完成後會成功的可執行指令）。infra commit 沒有 `Test case:` 這一行

### 刻意保留紅燈紀錄時（例外）

需要在 history 上保留紅燈證據時，才拆成兩個 commit：

```
test(<scope>): red - <紅燈描述> [<書中出處>]
feat(<scope>): <綠燈描述> [<書中出處>]
```

用這個寫法時，`test:` commit 是紅燈，必須在 body 說明紅燈出現在哪裡

### 共通規則

- `<scope>`：測試層級（`unit`/`integration`/`e2e`）或模組名（`ui`/`xmpp`...），哪個對這次改動更有辨識度就用哪個；跨很多模組時整個 scope 省略
- `<書中出處>`：章節（`ch10`）、小節（`3.6`）、頁碼（`p42`）可以視情況組合
- `test`/`feat` 的 commit body 一定要加一行 `Test case: <測試案例名稱>`
- 整個 commit message 一律只能用英文，不能出現中文字
