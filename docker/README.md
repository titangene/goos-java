# Docker 環境

## Java

Java 的編譯、執行、測試一律透過 `docker-compose.yml` 定義的 `toolbox` container 執行，不在 host 直接跑 `javac`/`java`。

| 腳本 | 用途 |
|---|---|
| `scripts/test.sh` | 編譯並執行 E2E 測試（`test.sh --headed` 會讓測試執行時的 Swing UI 顯示在主機螢幕上，方便除錯；預設不加參數是用 `xvfb-run` 虛擬 display，畫面不會顯示出來） |
