# 手動模擬 Sniper 加入拍賣、拍賣結束

`docker/tools/FakeAuction.java` 是一個互動式的假拍賣工具，透過 `docker/scripts/fake-auction.sh` 執行（環境設定見 [docker/README.md](../docker/README.md)）：

- 它用跟測試程式碼 `FakeAuctionServer.java` 相同的協定（`SOLVersion: 1.1; Event: PRICE; ...`），登入 `auction-<itemId>@localhost/Auction`
- 它扮演賣家，跟 `docker/scripts/run-app.sh` 開起來的 Auction Sniper app 手動互動

輸入格式是 SOL 訊息本身的內容，只省略固定不變的 `SOLVersion: 1.1; ` 前綴（工具會自動補上）。例如輸入 `Event: CLOSE;`，實際發出的就是 `SOLVersion: 1.1; Event: CLOSE;`

## 目前的實作範圍

目前實作到書中 12.4，以下是讀 `src/` 原始碼確認的：

- `AuctionMessageTranslator` 已經會依 `Event` 分派：`CLOSE` 呼叫 `auctionClosed()`，`PRICE` 呼叫 `currentPrice(price, increment)`
- `auctionsniper.Main` 的 `currentPrice(price, increment)` 還是空實作

因此送出 `Event: PRICE; ...` 目前不會反應在 UI 上，只有兩件事有實際效果：

- sniper 加入拍賣：app 顯示 `Joining`（`MainWindow.STATUS_JOINING`），假拍賣工具印出 `Sniper joined: <sniper JID>`
- 拍賣結束（`Event: CLOSE;`）：app 顯示 `Lost`（`MainWindow.STATUS_LOST`）

還沒有 `Bidding`/`Winning`/`Losing`/`Won` 這些狀態，等書中進度補上出價邏輯後再擴充本文件與這支工具。

## 注意：不要同時跑 e2e 測試

Openfire 的 resource conflict policy 設成 `Never kick`（見 [docker/README.md](../docker/README.md)）。假拍賣工具還連著 `auction-item-54321@localhost/Auction` 時跑 e2e 測試，`FakeAuctionServer.startSellingItem()` 用同一個帳號 + resource 登入會收到 `conflict(409)`，測試失敗。跑測試前先在假拍賣工具輸入 `quit`。

## 步驟

**1. 準備好 Docker 環境**（第一次要先跑 `docker/scripts/start-env.sh`，見 [docker/README.md](../docker/README.md)）

**2. 開一個新的終端機分頁，啟動假拍賣工具（扮演 `item-54321` 的賣家）：**

```bash
docker/scripts/fake-auction.sh item-54321
```

會印出：

```
Selling item item-54321 as auction-item-54321@localhost/Auction. Waiting for a sniper to join...

Type a SOL message body (without the "SOLVersion: 1.1; " prefix) to send it, e.g.:
  Event: PRICE; CurrentPrice: 90; Increment: 5; Bidder: other bidder;
  Event: CLOSE;
Type "quit" to disconnect and exit.
```

**3. 另開一個終端機分頁，啟動 app 並加入同一個 item：**

```bash
docker/scripts/run-app.sh item-54321
```

app 視窗會顯示 `Joining`。假拍賣工具會在終端機印出 `> Sniper joined: sniper@localhost/Auction`，代表 sniper 已經送出 JOIN 訊息

**4. 模擬拍賣結束**，在 `fake-auction.sh` 的終端機輸入：

```
Event: CLOSE;
```

app 視窗會變成顯示 `Lost`，終端機會印出 `> sent: SOLVersion: 1.1; Event: CLOSE;`

**5. 結束假拍賣工具：**

```
quit
```

以下是跑過 `docker/scripts/fake-auction.sh` 以上流程的 console：

```
Selling item item-54321 as auction-item-54321@localhost/Auction. Waiting for a sniper to join...

Type a SOL message body (without the "SOLVersion: 1.1; " prefix) to send it, e.g.:
  Event: PRICE; CurrentPrice: 90; Increment: 5; Bidder: other bidder;
  Event: CLOSE;
Type "quit" to disconnect and exit.

> Sniper joined: sniper@localhost/Auction

>>> Event: CLOSE;
> sent: SOLVersion: 1.1; Event: CLOSE;

>>> quit
```