SELECT i.id                    AS itemId,
       i.item_name             AS itemName,
       i.sell_counts           AS sellCounts,
       ii.url                  AS imgUrl,
       tempSpec.price_discount AS price,
       i.updated_time          as updated_time
FROM items i
         LEFT JOIN items_img ii ON i.id = ii.item_id
         LEFT JOIN (SELECT item_id, MIN(price_discount) AS price_discount
                    FROM items_spec
                    GROUP BY item_id) tempSpec
                   ON i.id = tempSpec.item_id
WHERE ii.is_main = 1
  and i.updated_time >= :sql_last_value
