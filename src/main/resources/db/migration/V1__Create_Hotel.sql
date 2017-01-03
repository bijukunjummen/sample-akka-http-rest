CREATE TABLE "HOTELS" (
  "hotel_id"       BIGINT  IDENTITY PRIMARY KEY,
  "hotel_name" VARCHAR(100) NOT NULL,
  "hotel_address" VARCHAR(250) NOT NULL,
  "zip" VARCHAR(15) NOT NULL
);