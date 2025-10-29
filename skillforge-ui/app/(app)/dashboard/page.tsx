"use client";

import * as React from "react";
import Link from "next/link";
import { ArrowRight, BookOpen, Download, Trophy, Workflow } from "lucide-react";

import { PageHeader } from "@/components/page-header";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from "@/components/ui/card";
import { Progress } from "@/components/ui/progress";
import { useToast } from "@/components/ui/use-toast";
import { assessments } from "@/mock-api/assessments";
import { badges } from "@/mock-api/badges";
import { certificates } from "@/mock-api/certificates";
import { courses } from "@/mock-api/courses";
import { jobs } from "@/mock-api/jobs";
import { labs } from "@/mock-api/labs";
import { leaderboard } from "@/mock-api/leaderboard";
import { programs } from "@/mock-api/programs";

export default function DashboardPage() {
  const { addToast } = useToast();
  const currentCourse = courses.find((course) => course.status === "in-progress");
  const nextLab = labs.find((lab) => lab.id === "lab-observability");
  const upcomingAssessments = assessments.filter((assessment) => assessment.status === "upcoming").slice(0, 2);
  const recommendations = programs.slice(0, 2);

  return (
    <div className="space-y-6">
      <PageHeader
        title="Student dashboard"
        description="Track your programme momentum, labs, and credentials in one place."
      />
      <div className="grid gap-6 lg:grid-cols-3">
        <Card className="lg:col-span-2">
          <CardHeader className="flex flex-col gap-2">
            <CardTitle className="flex items-center gap-2 text-xl">
              <BookOpen className="h-5 w-5" aria-hidden="true" /> Continue learning
            </CardTitle>
            <CardDescription>Pick up exactly where you left off.</CardDescription>
          </CardHeader>
          <CardContent className="space-y-6">
            {currentCourse ? (
              <div className="space-y-4">
                <div>
                  <h3 className="text-lg font-semibold">{currentCourse.title}</h3>
                  <p className="text-sm text-muted-foreground">Progress is synced across web and mobile.</p>
                </div>
                <div className="space-y-2">
                  <Progress value={currentCourse.progress} aria-label="Course progress" />
                  <p className="text-xs text-muted-foreground">{currentCourse.progress}% complete</p>
                </div>
                <Button asChild>
                  <Link href={`/programme/${currentCourse.programId}`}>Open module</Link>
                </Button>
              </div>
            ) : (
              <p className="text-sm text-muted-foreground">No active modules right now.</p>
            )}
          </CardContent>
        </Card>
        <Card>
          <CardHeader>
            <CardTitle className="text-xl">Badges & leaderboard</CardTitle>
            <CardDescription>You are # {leaderboard.rank} of {leaderboard.totalLearners} peers.</CardDescription>
          </CardHeader>
          <CardContent className="space-y-4">
            <div className="flex flex-wrap gap-2">
              {badges.map((badge) => (
                <Badge key={badge.id} className="gap-1 bg-secondary/60">
                  <span aria-hidden="true">{badge.icon}</span>
                  {badge.title}
                </Badge>
              ))}
            </div>
            <div className="rounded-2xl border border-dashed border-border p-3 text-sm text-muted-foreground">
              Latest climb: +3 positions this week. Keep collaborating in labs to unlock mentorship tokens.
            </div>
          </CardContent>
        </Card>
      </div>
      <div className="grid gap-6 lg:grid-cols-3">
        <Card className="lg:col-span-2">
          <CardHeader>
            <CardTitle className="text-xl">Upcoming assessments</CardTitle>
            <CardDescription>Plan focus time before due dates.</CardDescription>
          </CardHeader>
          <CardContent className="space-y-4">
            {upcomingAssessments.map((assessment) => (
              <div key={assessment.id} className="flex flex-col gap-3 rounded-2xl border border-border/80 p-4 md:flex-row md:items-center md:justify-between">
                <div>
                  <h3 className="text-lg font-semibold">{assessment.title}</h3>
                  <p className="text-sm text-muted-foreground">{assessment.relatedProgram}</p>
                </div>
                <div className="flex items-center gap-3">
                  <Badge variant={assessment.type === "quiz" ? "success" : "outline"}>
                    {assessment.type === "quiz" ? "Quiz" : "Project"}
                  </Badge>
                  <Badge variant="warning">Due {new Date(assessment.dueDate).toLocaleDateString()}</Badge>
                  <Button variant="outline" asChild>
                    <Link href={`/assessment/${assessment.id}`}>Open</Link>
                  </Button>
                </div>
              </div>
            ))}
          </CardContent>
        </Card>
        <Card>
          <CardHeader>
            <CardTitle className="text-xl">Virtual lab</CardTitle>
            <CardDescription>Ready for your next live environment.</CardDescription>
          </CardHeader>
          <CardContent className="space-y-4">
            {nextLab ? (
              <div className="space-y-3">
                <h3 className="font-semibold">{nextLab.title}</h3>
                <p className="text-sm text-muted-foreground">Attempts left: {nextLab.maxAttempts - nextLab.attempts}</p>
                <Button className="w-full" onClick={() => addToast({ title: "Lab launched", description: "Session timer started." })}>
                  Start lab
                </Button>
              </div>
            ) : (
              <p className="text-sm text-muted-foreground">No labs queued. Check the catalogue for new labs.</p>
            )}
          </CardContent>
        </Card>
      </div>
      <div className="grid gap-6 lg:grid-cols-3">
        <Card>
          <CardHeader>
            <CardTitle className="text-xl">AI recommendations</CardTitle>
            <CardDescription>Curated programmes aligned with your goals.</CardDescription>
          </CardHeader>
          <CardContent className="space-y-4">
            {recommendations.map((program) => (
              <div key={program.id} className="space-y-3 rounded-2xl border border-dashed border-border p-4">
                <div>
                  <h3 className="text-lg font-semibold">{program.title}</h3>
                  <p className="text-sm text-muted-foreground">Starts {new Date(program.startDate).toLocaleDateString()}</p>
                </div>
                <div className="flex items-center gap-2">
                  <Button size="sm" onClick={() => addToast({ title: "Saved", description: `${program.title} saved to your shortlist.` })}>
                    Save
                  </Button>
                  <Button
                    size="sm"
                    variant="ghost"
                    onClick={() => addToast({ title: "Dismissed", description: `We will show fewer recommendations like ${program.title}.` })}
                  >
                    Dismiss
                  </Button>
                </div>
              </div>
            ))}
          </CardContent>
        </Card>
        <Card>
          <CardHeader>
            <CardTitle className="text-xl flex items-center gap-2">
              <Download className="h-4 w-4" /> Offline study
            </CardTitle>
            <CardDescription>Sync content for areas with limited bandwidth.</CardDescription>
          </CardHeader>
          <CardContent className="space-y-3">
            <p className="text-sm text-muted-foreground">Last sync: 2 hours ago.</p>
            <Button variant="secondary" onClick={() => addToast({ title: "Download queued", description: "Modules will be available offline." })}>
              Download module
            </Button>
          </CardContent>
        </Card>
        <Card>
          <CardHeader>
            <CardTitle className="text-xl flex items-center gap-2">
              <Workflow className="h-4 w-4" /> Credential wallet
            </CardTitle>
            <CardDescription>Share verifiable achievements.</CardDescription>
          </CardHeader>
          <CardContent className="space-y-3">
            {certificates.map((certificate) => (
              <div key={certificate.id} className="space-y-2 rounded-2xl border border-border p-4">
                <div className="flex items-center justify-between">
                  <div>
                    <p className="text-sm font-semibold">{certificate.program}</p>
                    <p className="text-xs text-muted-foreground">Issued {new Date(certificate.issueDate).toLocaleDateString()}</p>
                  </div>
                  <Badge variant="success">{certificate.status}</Badge>
                </div>
                <Button
                  variant="outline"
                  size="sm"
                  onClick={() => {
                    navigator.clipboard.writeText(certificate.verifyUrl);
                    addToast({ title: "Link copied", description: "Share the verify URL with employers." });
                  }}
                >
                  Copy verify URL
                </Button>
              </div>
            ))}
          </CardContent>
        </Card>
      </div>
      <Card>
        <CardHeader>
          <CardTitle className="text-xl flex items-center gap-2">
            <Trophy className="h-5 w-5" /> Jobs & internships
          </CardTitle>
          <CardDescription>Matched based on your credentials and interest areas.</CardDescription>
        </CardHeader>
        <CardContent className="grid gap-4 md:grid-cols-2">
          {jobs.slice(0, 2).map((job) => (
            <div key={job.id} className="flex flex-col justify-between rounded-2xl border border-border p-4">
              <div className="space-y-2">
                <h3 className="text-lg font-semibold">{job.title}</h3>
                <p className="text-sm text-muted-foreground">{job.company} · {job.location}</p>
                <p className="text-sm text-muted-foreground">Requires: {job.requiredCerts.join(", ")}</p>
              </div>
              <Button className="mt-4" variant="secondary" asChild>
                <Link href={`/jobs#${job.id}`}>Apply</Link>
              </Button>
            </div>
          ))}
        </CardContent>
        <CardFooter>
          <Button variant="link" className="px-0" asChild>
            <Link href="/jobs" className="flex items-center gap-2">
              View all opportunities <ArrowRight className="h-4 w-4" />
            </Link>
          </Button>
        </CardFooter>
      </Card>
    </div>
  );
}
