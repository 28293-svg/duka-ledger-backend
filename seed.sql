-- Fallback seed for the FINAL code (D1 blocks past dates, so Step 0 through the API only works at Part A stage).
-- Run in pgAdmin Query Tool on db duka_ledger AFTER the app has started once (tables must exist). Needs PostgreSQL 13+.
insert into shop (id, shop_number, name) values
 (gen_random_uuid(), 1, 'Duka Centre'), (gen_random_uuid(), 2, 'Market Corner'),
 (gen_random_uuid(), 3, 'Station Shop'), (gen_random_uuid(), 4, 'Hill Top Shop');

insert into shopkeeper (id, first_name, last_name, phone, shop_id)
 select gen_random_uuid(), v.f, v.l, v.p, s.id
 from (values ('Eric','Habimana','0788111111',1), ('Grace','Mukamana','0788222222',2), ('Samuel','Niyonzima','0788333333',3)) v(f,l,p,n)
 join shop s on s.shop_number = v.n;

insert into shopkeeper_product_line (shopkeeper_id, product_line_id)
 select k.id, p.id from shopkeeper k, product_line p
 where (k.phone = '0788111111' and p.name in ('Groceries','Beverages'))
    or (k.phone = '0788222222' and p.name = 'Household Goods')
    or (k.phone = '0788333333' and p.name = 'Beverages');

insert into customer (id, first_name, last_name, phone, village) values
 (gen_random_uuid(), 'Alice',  'Rugema',   '0789012345', 'Kimironko'),
 (gen_random_uuid(), 'Jean',   'Rugema',   '0789012346', 'Remera'),
 (gen_random_uuid(), 'Claire', 'Uwase',    '0789012347', 'Nyamirambo'),
 (gen_random_uuid(), 'Patrick','Mugabo',   '0789012348', 'Kicukiro'),
 (gen_random_uuid(), 'Diane',  'Ingabire', '0789012349', 'Gikondo');

insert into credit_entry (id, entry_date, status, items, amount, customer_id, shopkeeper_id)
 select gen_random_uuid(), v.d::date, v.st, v.it, v.am, c.id, k.id
 from (values
  ('2026-10-05','RECORDED',   '#1 Rice 5kg and sugar',  9500, '0789012345','0788111111'),
  ('2026-10-12','OVERDUE',    '#2 Cooking oil 3L',      7800, '0789012345','0788111111'),
  ('2026-10-20','REPAID',     '#3 Soap and detergent',  4500, '0789012345','0788222222'),
  ('2026-10-20','WRITTEN_OFF','#4 Maize flour 10kg',   12000, '0789012346','0788111111'),
  ('2026-10-20','RECORDED',   '#5 Beans 5kg',           6000, '0789012346','0788111111'),
  ('2026-10-28','RECORDED',   '#6 Bread and milk',      3200, '0789012347','0788222222'),
  ('2026-11-03','REPAID',     '#7 Soda crate',          8000, '0789012347','0788333333'),
  ('2026-11-10','OVERDUE',    '#8 Salt and tea leaves', 2500, '0789012348','0788333333'),
  ('2026-11-15','RECORDED',   '#9 Candles and matches', 1800, '0789012349','0788222222'),
  ('2026-11-20','REPAID',     '#10 Rice 10kg',         18000, '0789012348','0788111111')
 ) v(d,st,it,am,cp,sp)
 join customer c on c.phone = v.cp
 join shopkeeper k on k.phone = v.sp;
