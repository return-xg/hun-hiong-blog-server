-- ============================================================
-- hun-hiong-blog-server 数据库初始化脚本
-- 数据库: PostgreSQL 16
-- 说明:   bigint 主键(应用层使用雪花算法生成), 时间字段使用 timestamp
-- ============================================================

-- 如需新建库，取消下面注释执行
-- CREATE DATABASE hun_hiong_blog WITH ENCODING 'UTF8';

-- ============================================================
-- 1. 系统用户表 sys_user
-- ============================================================
CREATE TABLE IF NOT EXISTS sys_user (
    id          BIGINT       PRIMARY KEY,
    username    VARCHAR(64)  NOT NULL,
    password    VARCHAR(128) NOT NULL,
    nickname    VARCHAR(64),
    avatar      VARCHAR(255),
    status      SMALLINT     NOT NULL DEFAULT 1,
    create_time TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE  sys_user IS '系统用户表';
COMMENT ON COLUMN sys_user.id          IS '主键ID';
COMMENT ON COLUMN sys_user.username    IS '用户名';
COMMENT ON COLUMN sys_user.password    IS '密码（BCrypt 加密）';
COMMENT ON COLUMN sys_user.nickname    IS '昵称';
COMMENT ON COLUMN sys_user.avatar      IS '头像URL';
COMMENT ON COLUMN sys_user.status      IS '状态：1-启用，0-禁用';
COMMENT ON COLUMN sys_user.create_time IS '创建时间';
COMMENT ON COLUMN sys_user.update_time IS '更新时间';

-- 唯一索引：用户名
CREATE UNIQUE INDEX IF NOT EXISTS uk_sys_user_username ON sys_user (username);
-- 普通索引：状态
CREATE INDEX IF NOT EXISTS idx_sys_user_status ON sys_user (status);


-- ============================================================
-- 2. 博客分类表 blog_category
-- ============================================================
CREATE TABLE IF NOT EXISTS blog_category (
    id          BIGINT       PRIMARY KEY,
    name        VARCHAR(64)  NOT NULL,
    slug        VARCHAR(128),
    sort        INTEGER      NOT NULL DEFAULT 0,
    description VARCHAR(255),
    create_time TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted     SMALLINT     NOT NULL DEFAULT 0
);

COMMENT ON TABLE  blog_category IS '博客分类表';
COMMENT ON COLUMN blog_category.id          IS '主键ID';
COMMENT ON COLUMN blog_category.name        IS '分类名称';
COMMENT ON COLUMN blog_category.slug        IS '分类别名（URL友好）';
COMMENT ON COLUMN blog_category.sort        IS '排序（升序）';
COMMENT ON COLUMN blog_category.description IS '分类描述';
COMMENT ON COLUMN blog_category.create_time IS '创建时间';
COMMENT ON COLUMN blog_category.update_time IS '更新时间';
COMMENT ON COLUMN blog_category.deleted     IS '逻辑删除：0-未删除，1-已删除';

-- 唯一索引：分类名称、slug（未删除范围内）
CREATE UNIQUE INDEX IF NOT EXISTS uk_blog_category_name ON blog_category (name) WHERE deleted = 0;
CREATE UNIQUE INDEX IF NOT EXISTS uk_blog_category_slug ON blog_category (slug) WHERE deleted = 0;
CREATE INDEX IF NOT EXISTS idx_blog_category_sort ON blog_category (sort);


-- ============================================================
-- 3. 博客标签表 blog_tag
-- ============================================================
CREATE TABLE IF NOT EXISTS blog_tag (
    id          BIGINT       PRIMARY KEY,
    name        VARCHAR(64)  NOT NULL,
    slug        VARCHAR(128),
    create_time TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted     SMALLINT     NOT NULL DEFAULT 0
);

COMMENT ON TABLE  blog_tag IS '博客标签表';
COMMENT ON COLUMN blog_tag.id          IS '主键ID';
COMMENT ON COLUMN blog_tag.name        IS '标签名称';
COMMENT ON COLUMN blog_tag.slug        IS '标签别名（URL友好）';
COMMENT ON COLUMN blog_tag.create_time IS '创建时间';
COMMENT ON COLUMN blog_tag.update_time IS '更新时间';
COMMENT ON COLUMN blog_tag.deleted     IS '逻辑删除：0-未删除，1-已删除';

CREATE UNIQUE INDEX IF NOT EXISTS uk_blog_tag_name ON blog_tag (name) WHERE deleted = 0;
CREATE UNIQUE INDEX IF NOT EXISTS uk_blog_tag_slug ON blog_tag (slug) WHERE deleted = 0;


-- ============================================================
-- 4. 博客文章表 blog_article
-- ============================================================
CREATE TABLE IF NOT EXISTS blog_article (
    id          BIGINT        PRIMARY KEY,
    title        VARCHAR(255) NOT NULL,
    summary     VARCHAR(500),
    content     TEXT,
    cover_url   VARCHAR(255),
    category_id BIGINT,
    status      SMALLINT      NOT NULL DEFAULT 0,
    view_count  BIGINT        NOT NULL DEFAULT 0,
    like_count  BIGINT        NOT NULL DEFAULT 0,
    create_time TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted     SMALLINT      NOT NULL DEFAULT 0
);

COMMENT ON TABLE  blog_article IS '博客文章表';
COMMENT ON COLUMN blog_article.id          IS '主键ID';
COMMENT ON COLUMN blog_article.title       IS '文章标题';
COMMENT ON COLUMN blog_article.summary     IS '文章摘要';
COMMENT ON COLUMN blog_article.content     IS '文章内容（Markdown/HTML）';
COMMENT ON COLUMN blog_article.cover_url   IS '封面图URL';
COMMENT ON COLUMN blog_article.category_id IS '分类ID';
COMMENT ON COLUMN blog_article.status      IS '状态：0-草稿，1-已发布，2-下线';
COMMENT ON COLUMN blog_article.view_count  IS '浏览量';
COMMENT ON COLUMN blog_article.like_count  IS '点赞量';
COMMENT ON COLUMN blog_article.create_time IS '创建时间';
COMMENT ON COLUMN blog_article.update_time IS '更新时间';
COMMENT ON COLUMN blog_article.deleted     IS '逻辑删除：0-未删除，1-已删除';

-- 索引：分类、状态、创建时间、标题（检索）
CREATE INDEX IF NOT EXISTS idx_blog_article_category_id ON blog_article (category_id);
CREATE INDEX IF NOT EXISTS idx_blog_article_status      ON blog_article (status);
CREATE INDEX IF NOT EXISTS idx_blog_article_create_time ON blog_article (create_time);
CREATE INDEX IF NOT EXISTS idx_blog_article_title       ON blog_article (title);


-- ============================================================
-- 5. 文章-标签关联表 blog_article_tag
-- ============================================================
CREATE TABLE IF NOT EXISTS blog_article_tag (
    id          BIGINT    PRIMARY KEY,
    article_id  BIGINT    NOT NULL,
    tag_id      BIGINT    NOT NULL,
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE  blog_article_tag IS '文章-标签关联表';
COMMENT ON COLUMN blog_article_tag.id          IS '主键ID';
COMMENT ON COLUMN blog_article_tag.article_id  IS '文章ID';
COMMENT ON COLUMN blog_article_tag.tag_id      IS '标签ID';
COMMENT ON COLUMN blog_article_tag.create_time IS '创建时间';

-- 唯一索引：同一文章同一标签只能关联一次
CREATE UNIQUE INDEX IF NOT EXISTS uk_blog_article_tag ON blog_article_tag (article_id, tag_id);
-- 普通索引：反向查询
CREATE INDEX IF NOT EXISTS idx_blog_article_tag_tag_id ON blog_article_tag (tag_id);


-- ============================================================
-- 初始化数据（可选）：默认管理员账户
-- 密码: admin123 （BCrypt 加密）
-- 生产环境请务必修改！
-- ============================================================
INSERT INTO sys_user (id, username, password, nickname, status)
VALUES (1, 'admin', '$2a$10$8pW.zTP9lfWn.ehbz2aK2u3OsScojz0YhqLp0kyH.7Blsjdrf8M1m', '管理员', 1)
ON CONFLICT (id) DO NOTHING;
