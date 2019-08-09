select * from users limit 5;

select * from users u inner join rates r on u.user_id=r.user_id inner join movies m on r.movie_id=m.movie_id;

/**
按性别分级的每部电影的平均电影评分
*/
select t.movie_id, t.title, sum(t.female) as F, sum(t.male) as M 
from (
	select m.movie_id, m.title, 
           avg(case when (u.gender = 'F') then  rating else 0 end) as female,
	       avg(case when(u.gender = 'M') then rating else 0 end) as male
	from rates r, users u, movies m
	where r.movie_id=m.movie_id and r.user_id=u.user_id
	group by m.movie_id, m.title, u.gender
) t
group by t.movie_id, t.title;

/**
筛选出大于250个有评分的电影
*/
select m.movie_id, m.title, count(*) as cnt 
from movies m, rates r
where m.movie_id=r.movie_id
group by m.movie_id, m.title
having count(*)>250;

/**
筛选出大于250个有评分的电影，按性别分级的每部电影的平均电影评分
*/
select p1.* 
from (
    select t.movie_id, t.title, sum(t.female) as F, sum(t.male) as M 
    from (
	    select m.movie_id, m.title, 
            avg(case when (u.gender = 'F') then  rating else 0 end) as female,
	        avg(case when(u.gender = 'M') then rating else 0 end) as male
	    from rates r, users u, movies m
	    where r.movie_id=m.movie_id and r.user_id=u.user_id
	    group by m.movie_id, m.title, u.gender
    ) t
    group by t.movie_id, t.title
) p1, (
    select m.movie_id, m.title, count(*) as cnt 
    from movies m, rates r
	where m.movie_id=r.movie_id
	group by m.movie_id, m.title
	having count(*)>250
) p2
where p1.movie_id=p2.movie_id;

/**
筛选出大于250个有评分的电影，按性别分级的每部电影的平均电影评分，并按照女性观众电影评分排行榜
*/
select p1.* 
from (
    select t.movie_id, t.title, sum(t.female) as F, sum(t.male) as M 
    from (
	    select m.movie_id, m.title, 
            avg(case when (u.gender = 'F') then  rating else 0 end) as female,
	        avg(case when(u.gender = 'M') then rating else 0 end) as male
	    from rates r, users u, movies m
	    where r.movie_id=m.movie_id and r.user_id=u.user_id
	    group by m.movie_id, m.title, u.gender
    ) t
    group by t.movie_id, t.title
    ) p1, (
        select m.movie_id, m.title, count(*) as cnt from movies m, rates r
	    where m.movie_id=r.movie_id
	    group by m.movie_id, m.title
	    having count(*)>250
    ) p2
where p1.movie_id=p2.movie_id
order by p1.F desc;

/**
筛选出大于250个有评分的电影，按性别分级的每部电影的平均电影评分
男性首选电影，女性评分不高
*/
select n.*, (n.M-n.F) as diff 
from (
    select p1.* from (select t.movie_id, t.title, sum(t.female) as F, sum(t.male) as M 
    from (
	    select m.movie_id, m.title, 
            avg(case when (u.gender = 'F') then  rating else 0 end) as female,
	        avg(case when(u.gender = 'M') then rating else 0 end) as male
	    from rates r, users u, movies m
	    where r.movie_id=m.movie_id and r.user_id=u.user_id
	    group by m.movie_id, m.title, u.gender
    ) t
    group by t.movie_id, t.title
    ) p1, (
        select m.movie_id, m.title, count(*) as cnt from movies m, rates r
	    where m.movie_id=r.movie_id
	    group by m.movie_id, m.title
	    having count(*)>250
    ) p2
where p1.movie_id=p2.movie_id) n
order by diff asc;

/**
筛选出大于250个有评分的电影，按性别分级的每部电影的平均电影评分
女性首选电影，男性评分不高
*/
select n.*, (n.F-n.M) as diff 
from (
    select p1.* 
    from (
        select t.movie_id, t.title, sum(t.female) as F, sum(t.male) as M 
        from (
	        select m.movie_id, m.title, 
                avg(case when (u.gender = 'F') then  rating else 0 end) as female,
	            avg(case when(u.gender = 'M') then rating else 0 end) as male
	        from rates r, users u, movies m
	        where r.movie_id=m.movie_id and r.user_id=u.user_id
	        group by m.movie_id, m.title, u.gender
        ) t
        group by t.movie_id, t.title
    ) p1, (
        select m.movie_id, m.title, count(*) as cnt from movies m, rates r
	    where m.movie_id=r.movie_id
	    group by m.movie_id, m.title
	    having count(*)>250
    ) p2
where p1.movie_id=p2.movie_id) n
order by diff asc;


/**
计算每部电影的总平均评分及评分总个数
*/
select movie_id, avg(rating), count(*)
from rates
group by movie_id;


/**
取到每部电影的每条评分
*/
select movie_id, rating 
from rates;