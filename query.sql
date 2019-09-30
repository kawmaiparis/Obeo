SELECT t1.user_id
FROM holidays AS t1
         INNER JOIN
     (
         SELECT user_id, MAX(holiday_id) AS maxholiday
         FROM holidays where destination_city = 'london'
         GROUP BY user_id
     ) AS t2  ON t1.user_id       = t2.user_id
         AND t1.holiday_id = t2.maxholiday;
