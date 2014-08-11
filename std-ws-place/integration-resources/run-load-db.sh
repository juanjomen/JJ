#!/bin/bash

# -------------------------------------------------------------------------------------------------
# Run the three DB scripts
# -------------------------------------------------------------------------------------------------

sudo -u postgres psql -d p124 -f std-place-api-postgres-drop-db.sql > db-log.txt 2> db-log.txt
sudo -u postgres psql -d p124 -f std-place-api-postgres.sql         > db-log.txt 2> db-log.txt
sudo -u postgres psql -d p124 -f load-base-values.sql               > db-log.txt 2> db-log.txt
