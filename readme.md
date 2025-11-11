## this spring boot repo contains two microservice. 

#### the first one is kafka-producer, managing inventory update via REST api.

#### the second one is kafka-consumer, transfer the message from kafka to postgresql.

_____
## consumer topic 

shop-realtime-inventory topic 是店內貨架上商品被消費走的時候，對庫存做的更新。由於櫃台數量限制，同時間結帳人數有限，同時結到一個貨品的機率低。 更新庫存時應選用 optimistic lock。 

內部使用的 inventory-batch-update topic 則是每日倉庫結算、批量進貨等情況在使用的。 由於使用 batch-update，搭配 row lock 的話會導致鎖住時間過長，造成瓶頸。 因此選用 optimistic lock 配合 retry。

_____
## db inventory-log entity design 

此系統有審計上的嚴格對帳需求，需要知道某時刻的準確庫存。如果只將庫存的 delta 值放入 log。對於庫存的追蹤將變得很不可靠：

審計時得依靠 log 的加總去回推當時的數值　→　只要其中一筆 log 錯誤，數值便不準確。 

1.送出去的 api 沒有一定被記錄到。可能網路出問題 然後 retry 沒有處理好。
2.順序問題，partition 之間，在處理入庫是無序的。 即使 log 本來以 1 2 3 順序被撰寫。 但因為分給三個單獨的 partition 去處理，最後 consumer 處理好的順序可能是 2 1 3 。 

所以， inventory log 中包含了 old quantity 、new quantity。 在每一筆紀錄時，都要去讀取 db 中的舊值，加上 delta 得到新值，最後才入庫。原本 sql 操作是單純的寫入，現在變成讀 + 寫。這會犧牲效能，但換得更嚴謹的系統，