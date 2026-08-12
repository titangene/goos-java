# goos-java

參考 [GOOS（Growing Object-Oriented Software, Guided by Tests）](https://www.growing-object-oriented-software.com/) 書中的 Auction Sniper 範例，重新用 TDD 刻一次，盡量比照作者原始的 [goos-code](https://github.com/sf105/goos-code) repo 的簡單管理方式（vendor jar + 手動 `javac`/`java`，不用額外的建置工具）。專案介紹見 [README.md](README.md)。

## Java 一律用 Docker 執行

本專案的 Java（編譯、執行、測試）一律透過 `docker/docker-compose.yml` 定義的 `toolbox` container 執行，不在 host 直接跑 `javac`/`java`。

跑 end-to-end 測試：`docker/scripts/test.sh`（會啟動 `toolbox` container，並在其中執行 `docker/scripts/run-e2e-tests.sh`）。

跑 Swing app（視窗顯示在主機的 X display 上）：`docker/scripts/run-app.sh`。

## Openfire（XMPP）

E2E 測試需要連到真的 XMPP server，`toolbox` 用 `network_mode: service:openfire` 連到 Docker 跑的 Openfire（`XMPP_HOSTNAME` 寫死 `"localhost"`）。第一次建立環境（或 Openfire 資料被清空後）要先跑 `docker/scripts/start-env.sh`，之後才能跑 `docker/scripts/test.sh`。完整流程見 [docker/README.md](docker/README.md)。

## 依賴套件：vendor jar

依賴的 jar 放在 `lib/`，不用 Maven/Gradle 管理版本，直接 commit 進 git。新增依賴時：runtime 需要的放 `lib/deploy`，只有開發/測試用的放 `lib/develop`（新 jar 丟進去，兩個 IDE 都會自動抓到，不用改設定檔）。編譯 app 只用 `lib/deploy`；編譯/跑測試是 `lib/deploy` + `lib/develop`（`-src.jar` 要排除，`docker/scripts/run-e2e-tests.sh` 裡已經處理）。完整說明見 [README.md](README.md)。

## TDD commit message 格式

照書中章節逐步先寫 test code 再寫 production code，commit message 用 Conventional Commits，格式：

```
test(<scope>): red - <測試案例名稱> [<書中出處>]
feat(<scope>): green - <測試案例名稱> [<書中出處>]
refactor(<scope>): <重構描述> [<書中出處>]
```

- `<scope>`：測試層級（`unit`/`integration`/`e2e`）或模組名（`ui`/`api`/`redis`...），哪個對這次改動更有辨識度就用哪個；跨很多模組時整個 scope 省略
- `<書中出處>`：章節（`ch10`）、小節（`3.6`）、頁碼（`p42`）可以視情況組合，例如 `[3.6]`、`[p42]`、`[ch10 p85]`、`[3.6 p42]`，代表這個 commit 的內容涵蓋到書中這個章節/頁碼為止（不是精確定位在單一段落）

範例：

```
test(e2e): red - sniperJoinsAuctionUntilAuctionCloses [3.6]
feat(e2e): green - sniperJoinsAuctionUntilAuctionCloses [3.6]
refactor(ui): extract AuctionEventListener [p42]
test(e2e): red - sniperJoinsAuctionUntilAuctionCloses [ch10 p85]
test(e2e): red - sniperJoinsAuctionUntilAuctionCloses [11.2.1 p96]
```

如果使用者貼了書中內文當補充說明，body 不要照抄，改成精簡摘要，且**只能用英文，不能出現中文字**（避免混用中英文的怪 commit）。
