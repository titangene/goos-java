# goos-java

參考 [GOOS（Growing Object-Oriented Software, Guided by Tests）](https://www.growing-object-oriented-software.com/) 書中的 Auction Sniper 範例，重新用 TDD 刻一次，盡量比照作者原始的 [goos-code](https://github.com/sf105/goos-code) repo 的簡單管理方式（vendor jar + 手動 `javac`/`java`，不用額外的建置工具）。專案介紹見 [README.md](README.md)。

## Java 一律用 Docker 執行

本專案的 Java（編譯、執行、測試）一律透過 `docker/docker-compose.yml` 定義的 `toolbox` container 執行，不在 host 直接跑 `javac`/`java`。

跑全部測試（先 unit 再 end-to-end）：`docker/scripts/test.sh`。

跑 unit 測試：`docker/scripts/test-unit-tests.sh`（會啟動 `toolbox` container，並在其中執行 `docker/scripts/run-unit-tests.sh`；不需要 Openfire，但 `toolbox` 的 `network_mode: service:openfire` 設定仍會連帶啟動 `openfire` container）。

跑 end-to-end 測試：`docker/scripts/test-e2e-tests.sh`（會啟動 `toolbox` container，並在其中執行 `docker/scripts/run-e2e-tests.sh`）。

跑 Swing app（視窗顯示在主機的 X display 上）：`docker/scripts/run-app.sh <itemId> [username] [password]`。

## Openfire（XMPP）

E2E 測試需要連到真的 XMPP server，`toolbox` 用 `network_mode: service:openfire` 連到 Docker 跑的 Openfire（`XMPP_HOSTNAME` 寫死 `"localhost"`）。第一次建立環境（或 Openfire 資料被清空後）要先跑 `docker/scripts/start-env.sh`，之後才能跑 `docker/scripts/test.sh`。完整流程見 [docker/README.md](docker/README.md)。

## 依賴套件：vendor jar

依賴的 jar 放在 `lib/`，不用 Maven/Gradle 管理版本，直接 commit 進 git。新增依賴時：runtime 需要的放 `lib/deploy`，只有開發/測試用的放 `lib/develop`（新 jar 丟進去，兩個 IDE 都會自動抓到，不用改設定檔）。編譯 app 只用 `lib/deploy`；編譯/跑測試是 `lib/deploy` + `lib/develop`（`-src.jar` 要排除，`docker/scripts/run-e2e-tests.sh` 裡已經處理）。完整說明見 [README.md](README.md)。

## TDD commit message 格式

照書中章節逐步先寫 test code 再寫 production code，commit message 用 Conventional Commits，格式：

```
test(<scope>): red - <紅燈描述> [<書中出處>]
feat(<scope>): green - <綠燈描述> [<書中出處>]
refactor(<scope>): <重構描述> [<書中出處>]
```

- `<scope>`：測試層級（`unit`/`integration`/`e2e`）或模組名（`ui`/`api`/`redis`...），哪個對這次改動更有辨識度就用哪個；跨很多模組時整個 scope 省略
- `<紅燈描述>`/`<綠燈描述>`：精簡描述這次紅燈/綠燈的重點，不是完整測試方法名稱（完整測試方法名稱長，放進 subject 容易超過 Conventional Commits 建議的 50～72 字元上限）
- `<書中出處>`：章節（`ch10`）、小節（`3.6`）、頁碼（`p42`）可以視情況組合，例如 `[3.6]`、`[p42]`、`[ch10 p85]`、`[3.6 p42]`，代表這個 commit 的內容涵蓋到書中這個章節/頁碼為止（不是精確定位在單一段落）

`test`/`feat` 的 commit body 一定要加一行 `Test case: <測試案例名稱>`，補上被 subject 省略的完整測試方法名稱：

```
Test case: sniperJoinsAuctionUntilAuctionCloses
```

範例：

```
test(e2e): red - missing "Lost" status on close [11.2.1 p96]

Test case: sniperJoinsAuctionUntilAuctionCloses
```

```
feat(e2e): green - shows "Lost" when auction closes [11.2.4 p102]

Test case: sniperJoinsAuctionUntilAuctionCloses
```

```
refactor(ui): extract AuctionEventListener [p42]
```

整個 commit message（subject 與 body，包含 `<紅燈描述>`/`<綠燈描述>`/`<重構描述>`）一律只能用英文，不能出現中文字（避免混用中英文的怪 commit）。

如果使用者貼了書中內文當補充說明，body 除了 `Test case:` 那行以外，一定要加一行精簡摘要，不可省略、不可照抄書中原文。
