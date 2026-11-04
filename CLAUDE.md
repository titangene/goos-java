# goos-java

用 TDD 重刻 GOOS 書中的 Auction Sniper 範例。專案介紹與使用方式見 [README.md](README.md)。

## 執行 Java

Java 的編譯、執行、測試一律透過腳本在 `toolbox` container 內執行，不在 host 直接跑 `javac`/`java`：

- 全部測試：`scripts/test.sh`
- 單元測試：`scripts/test.sh unit`
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

## Commit message

一律只用英文，格式遵循 Conventional Commits：

```
test(<scope>): red - <紅燈描述> [<書中出處>]
feat(<scope>): green - <綠燈描述> [<書中出處>]
refactor(<scope>): <重構描述> [<書中出處>]
```

- `<scope>`：測試層級（`unit`/`integration`/`e2e`）或模組名（`ui`/`api`/`redis`...），選辨識度高的；跨很多模組時省略
- `<紅燈描述>`/`<綠燈描述>`：精簡描述重點，不寫完整測試方法名稱
- `<書中出處>`：這個 commit 涵蓋到書中的位置，可組合章節、小節、頁碼，例如 `[3.6]`、`[p42]`、`[ch10 p85]`、`[3.6 p42]`
- `test`/`feat` 的 body 一定要加 `Test case: <完整測試方法名稱>`
- 使用者貼了書中內文當補充說明時，body 要再加一行精簡摘要，不可照抄原文

範例：

```
test(e2e): red - missing "Lost" status on close [11.2.1 p96]

Test case: sniperJoinsAuctionUntilAuctionCloses
```

```
refactor(ui): extract AuctionEventListener [p42]
```
