# goos-java

參考 [GOOS（Growing Object-Oriented Software, Guided by Tests）](https://www.growing-object-oriented-software.com/) 書中的 Auction Sniper 範例，重新用 TDD 刻一次，盡量比照作者原始的 [goos-code](https://github.com/sf105/goos-code) repo 的簡單管理方式（vendor jar + 手動 `javac`/`java`，不用額外的建置工具）。專案介紹見 [README.md](README.md)。

## Java 一律用 Docker 執行

本專案的 Java（編譯、執行、測試）一律透過 `docker/docker-compose.yml` 定義的 `toolbox` container 執行，不在 host 直接跑 `javac`/`java`。

- 跑全部測試：`docker/scripts/test.sh`
- 跑 end-to-end 測試：`docker/scripts/test-e2e-tests.sh`（會啟動 `toolbox` container，並在其中執行 `docker/scripts/run-e2e-tests.sh`）

## 依賴套件：vendor jar

依賴的 jar 放在 `lib/`，不用 Maven/Gradle 等建置工具管理版本，直接 commit 進 git。新增依賴時：runtime 需要的放 `lib/deploy`，只有開發/測試用的放 `lib/develop`（新 jar 丟進去，兩個 IDE 都會自動抓到，不用改設定檔）。編譯 app 只用 `lib/deploy`；編譯/跑測試是 `lib/deploy` + `lib/develop`（`-src.jar` 要排除）。完整說明見 [README.md](README.md)。

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
