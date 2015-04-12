#ifndef FLASHBACK_COMPASS_H
#define FLASHBACK_COMPASS_H

#define ARROW_WIDTH 20
#define ARROW_HEIGHT 30
#define GRANULARITY 114
#define THRESHOLD 50
#define CUR_X 0
#define CUR_Y 1
#define END_X 2
#define END_Y 3
#define DIR 4
#define DIST 5

typedef struct {
  float x, y;
} Location;

#endif