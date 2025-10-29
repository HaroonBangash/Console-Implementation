import * as React from "react";

import { cn } from "@/lib/utils";

const Progress = React.forwardRef<HTMLDivElement, React.HTMLAttributes<HTMLDivElement> & { value?: number }>(
  ({ className, value = 0, ...props }, ref) => (
    <div ref={ref} className={cn("h-3 w-full rounded-full bg-muted", className)} role="progressbar" aria-valuenow={value} {...props}>
      <div className="h-3 rounded-full bg-primary transition-all" style={{ width: `${value}%` }} />
    </div>
  )
);
Progress.displayName = "Progress";

export { Progress };
