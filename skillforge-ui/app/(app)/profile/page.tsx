"use client";

import * as React from "react";

import { PageHeader } from "@/components/page-header";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";

export default function ProfilePage() {
  return (
    <div className="space-y-6">
      <PageHeader title="Profile" description="Manage your personal details and notification preferences." />
      <Card>
        <CardHeader>
          <CardTitle>Profile settings coming soon</CardTitle>
        </CardHeader>
        <CardContent className="text-sm text-muted-foreground">
          This demo focuses on showcasing core learning flows. Profile management will connect to identity services in a full build.
        </CardContent>
      </Card>
    </div>
  );
}
