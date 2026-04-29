import csv

ROWS = 10000

with open("post_10000k.csv", "w", newline="", encoding="utf-8") as f:
    writer = csv.writer(f)
    writer.writerow(["userId", "title", "content", "view_cnt", "created_user", "updated_user"])

    for i in range(1, ROWS + 1):
        writer.writerow([
            f"dmswo106",
            f"맛있는 음식점은 어디에있나?? {i}",
            f"우리 함께 찾아볼까??",
            0,
            "system",
            "system"
        ])

print("CSV 생성 완료: post_10000k.csv")