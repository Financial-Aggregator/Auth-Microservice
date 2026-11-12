CREATE TABLE user_credentials (
                                  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                  user_id UUID NOT NULL UNIQUE,
                                  username VARCHAR(100) NOT NULL UNIQUE,
                                  password VARCHAR(255) NOT NULL
);