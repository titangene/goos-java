# goos-java

參考 [GOOS（Growing Object-Oriented Software, Guided by Tests）](https://www.growing-object-oriented-software.com/) 書中的 Auction Sniper 範例，重新用 TDD 刻一次，盡量比照作者原始的 [goos-code](https://github.com/sf105/goos-code) repo 的簡單管理方式（vendor jar + 手動 `javac`/`java`，不用額外的建置工具）。專案介紹見 [README.md](README.md)。

## Java 一律用 Docker 執行

本專案的 Java（編譯、執行、測試）一律透過 `docker/docker-compose.yml` 定義的 `toolbox` container 執行，不在 host 直接跑 `javac`/`java`。

跑 end-to-end 測試：`docker/scripts/test.sh`（會啟動 `toolbox` container，並在其中執行 `docker/scripts/run-e2e-tests.sh`）。

## 依賴套件：vendor jar

依賴的 jar 放在 `lib/`，不用 Maven/Gradle 管理版本，直接 commit 進 git。新增依賴時：runtime 需要的放 `lib/deploy`，只有開發/測試用的放 `lib/develop`（新 jar 丟進去，兩個 IDE 都會自動抓到，不用改設定檔）。編譯 app 只用 `lib/deploy`；編譯/跑測試是 `lib/deploy` + `lib/develop`（`-src.jar` 要排除，`docker/scripts/run-e2e-tests.sh` 裡已經處理）。完整說明見 [README.md](README.md)。
