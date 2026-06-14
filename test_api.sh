gem#!/bin/bash

# 后端 API 测试脚本 - 适用于 WSL/Linux
# 自动检测宿主机 IP (针对 WSL 访问 Windows 后端)

# 1. 自动检测地址
HOST_IP="localhost"
if ! curl -s --connect-timeout 1 http://localhost:8080/api/canteens > /dev/null; then
    # 尝试从 resolv.conf 获取宿主机 IP
    POTENTIAL_HOST=$(grep nameserver /etc/resolv.conf | awk '{print $2}')
    if curl -s --connect-timeout 1 http://$POTENTIAL_HOST:8080/api/canteens > /dev/null; then
        HOST_IP=$POTENTIAL_HOST
    fi
fi

BASE_URL="http://$HOST_IP:8080/api"
TOKEN=""
DISH_ID=""
CANTEEN_ID=""

# 颜色定义
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo "=== 开始后端接口联调测试 ==="
echo "目标地址: $BASE_URL"

# 1. 注册测试
echo -e "\n[1/7] 测试注册接口..."
USERNAME="testuser_$(date +%s)"
REGISTER_OUT=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "'$USERNAME'",
    "password": "password123",
    "nickname": "测试助手"
  }')

REGISTER_BODY=$(echo "$REGISTER_OUT" | head -n -1)
REGISTER_STATUS=$(echo "$REGISTER_OUT" | tail -n 1)

if [ "$REGISTER_STATUS" -eq 200 ] && echo "$REGISTER_BODY" | grep -q '"code":0'; then
    echo -e "${GREEN}注册成功 (HTTP $REGISTER_STATUS)${NC}"
    TOKEN=$(echo "$REGISTER_BODY" | grep -oP '"token":"\K[^"]+')
else
    echo -e "${RED}注册失败!${NC}"
    echo -e "HTTP 状态码: $REGISTER_STATUS"
    echo -e "返回内容: $REGISTER_BODY"
    exit 1
fi

# 2. 登录测试
echo -e "\n[2/7] 测试登录接口..."
LOGIN_RES=$(curl -s -X POST "$BASE_URL/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "'$USERNAME'",
    "password": "password123"
  }')

if echo "$LOGIN_RES" | grep -q '"code":0'; then
    echo -e "${GREEN}登录成功${NC}"
else
    echo -e "${RED}登录失败: $LOGIN_RES${NC}"
fi

# 3. 获取食堂列表
echo -e "\n[3/7] 获取食堂列表..."
CANTEENS_RES=$(curl -s -X GET "$BASE_URL/canteens")
if echo "$CANTEENS_RES" | grep -q '"code":0'; then
    CANTEEN_ID=$(echo "$CANTEENS_RES" | grep -oP '"id":\K[0-9]+' | head -n 1)
    echo -e "${GREEN}获取成功，第一个食堂ID: $CANTEEN_ID${NC}"
else
    echo -e "${RED}获取食堂列表失败: $CANTEENS_RES${NC}"
fi

# 4. 获取菜品列表
echo -e "\n[4/7] 获取菜品列表..."
DISHES_RES=$(curl -s -X GET "$BASE_URL/dishes?canteenId=$CANTEEN_ID")
if echo "$DISHES_RES" | grep -q '"code":0'; then
    DISH_ID=$(echo "$DISHES_RES" | grep -oP '"id":\K[0-9]+' | head -n 1)
    echo -e "${GREEN}获取成功，第一个菜品ID: $DISH_ID${NC}"
else
    echo -e "${RED}获取菜品列表失败: $DISHES_RES${NC}"
fi

# 5. 测试添加收藏
echo -e "\n[5/7] 测试添加收藏..."
FAV_ADD_RES=$(curl -s -X POST "$BASE_URL/users/me/favorites" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"dishId": '$DISH_ID'}')

if echo "$FAV_ADD_RES" | grep -q '"code":0'; then
    echo -e "${GREEN}添加收藏成功${NC}"
else
    echo -e "${RED}添加收藏失败: $FAV_ADD_RES${NC}"
fi

# 6. 获取收藏列表
echo -e "\n[6/7] 获取收藏列表..."
FAV_LIST_RES=$(curl -s -X GET "$BASE_URL/users/me/favorites" \
  -H "Authorization: Bearer $TOKEN")

if echo "$FAV_LIST_RES" | grep -q '"code":0'; then
    echo -e "${GREEN}获取收藏成功${NC}"
else
    echo -e "${RED}获取收藏失败: $FAV_LIST_RES${NC}"
fi

# 7. 取消收藏
echo -e "\n[7/7] 测试取消收藏 (Delete)..."
FAV_DEL_RES=$(curl -s -X DELETE "$BASE_URL/users/me/favorites/$DISH_ID" \
  -H "Authorization: Bearer $TOKEN")

if echo "$FAV_DEL_RES" | grep -q '"code":0'; then
    echo -e "${GREEN}取消收藏成功${NC}"
else
    echo -e "${RED}取消收藏失败: $FAV_DEL_RES${NC}"
fi

echo -e "\n=== 测试完成 ==="
