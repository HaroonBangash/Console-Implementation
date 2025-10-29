import * as React from "react";

export default function AuthLayout({ children }: { children: React.ReactNode }) {
  return <div className="flex min-h-screen flex-col bg-muted/20">{children}</div>;
}
