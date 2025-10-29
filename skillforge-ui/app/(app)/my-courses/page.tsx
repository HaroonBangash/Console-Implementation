"use client";

import * as React from "react";
import Link from "next/link";
import { Layers } from "lucide-react";

import { PageHeader } from "@/components/page-header";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Progress } from "@/components/ui/progress";
import { courses } from "@/mock-api/courses";

export default function MyCoursesPage() {
  return (
    <div className="space-y-6">
      <PageHeader title="My courses" description="Your in-progress and completed SkillForge modules." />
      <div className="grid gap-6 md:grid-cols-2">
        {courses.map((course) => (
          <Card key={course.id}>
            <CardHeader>
              <CardTitle className="flex items-center gap-2 text-xl">
                <Layers className="h-5 w-5" aria-hidden="true" /> {course.title}
              </CardTitle>
              <CardDescription>Status: {course.status.replace("-", " ")}</CardDescription>
            </CardHeader>
            <CardContent className="space-y-4">
              <Progress value={course.progress} aria-label={`${course.title} progress`} />
              <div className="flex items-center justify-between text-sm text-muted-foreground">
                <span>{course.progress}% complete</span>
                <span>Next: {course.nextStep ?? "Continue"}</span>
              </div>
              <Button asChild>
                <Link href={`/programme/${course.programId}`}>Open programme</Link>
              </Button>
            </CardContent>
          </Card>
        ))}
      </div>
    </div>
  );
}
