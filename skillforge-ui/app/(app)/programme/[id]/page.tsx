"use client";

import * as React from "react";
import Link from "next/link";
import { useParams, useRouter } from "next/navigation";
import { AlertCircle, BadgeCheck, BookOpen, ListChecks } from "lucide-react";

import { PageHeader, PageHeaderPrimaryAction } from "@/components/page-header";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { useToast } from "@/components/ui/use-toast";
import { assessments } from "@/mock-api/assessments";
import { courses } from "@/mock-api/courses";
import { programs } from "@/mock-api/programs";

export default function ProgrammeDetailPage() {
  const params = useParams<{ id: string }>();
  const router = useRouter();
  const { addToast } = useToast();
  const program = programs.find((item) => item.id === params?.id);
  const [enrolled, setEnrolled] = React.useState(false);

  const relatedModules = courses.filter((course) => course.programId === program?.id);
  const relatedAssessments = assessments.filter((assessment) =>
    assessment.relatedProgram.toLowerCase().includes(program?.title.split(" ")[0].toLowerCase() ?? "")
  );

  if (!program) {
    return (
      <div className="space-y-6">
        <PageHeader title="Programme not found" description="Try browsing the catalogue for another pathway." />
        <Button asChild>
          <Link href="/catalogue">Back to catalogue</Link>
        </Button>
      </div>
    );
  }

  const handleEnrol = () => {
    setEnrolled(true);
    addToast({ title: "Enrolled!", description: `You're enrolled in ${program.title}.` });
  };

  return (
    <div className="space-y-6">
      <PageHeader
        title={program.title}
        description={`${program.durationWeeks}-week immersive experience focused on ${program.industry.toLowerCase()} workflows.`}
        actions={
          enrolled ? (
            <PageHeaderPrimaryAction asChild>
              <Link href="/dashboard">Go to dashboard</Link>
            </PageHeaderPrimaryAction>
          ) : (
            <PageHeaderPrimaryAction onClick={handleEnrol}>Enrol</PageHeaderPrimaryAction>
          )
        }
      />
      <Card>
        <CardContent className="grid gap-4 p-6 md:grid-cols-4">
          <div>
            <h3 className="text-sm font-semibold uppercase tracking-wide text-muted-foreground">Industry</h3>
            <p className="mt-1 text-lg font-semibold">{program.industry}</p>
          </div>
          <div>
            <h3 className="text-sm font-semibold uppercase tracking-wide text-muted-foreground">Duration</h3>
            <p className="mt-1 text-lg font-semibold">{program.durationWeeks} weeks</p>
          </div>
          <div>
            <h3 className="text-sm font-semibold uppercase tracking-wide text-muted-foreground">Next cohort</h3>
            <p className="mt-1 text-lg font-semibold">{new Date(program.startDate).toLocaleDateString()}</p>
          </div>
          <div>
            <h3 className="text-sm font-semibold uppercase tracking-wide text-muted-foreground">Outcome</h3>
            <p className="mt-1 text-lg font-semibold">{program.outcome}</p>
          </div>
        </CardContent>
      </Card>
      <Tabs defaultValue="overview" className="space-y-4">
        <TabsList>
          <TabsTrigger value="overview">Overview</TabsTrigger>
          <TabsTrigger value="modules">Modules</TabsTrigger>
          <TabsTrigger value="assessments">Assessments</TabsTrigger>
          <TabsTrigger value="certifications">Certifications</TabsTrigger>
        </TabsList>
        <TabsContent value="overview">
          <div className="space-y-4">
            <p className="text-sm leading-6 text-muted-foreground">
              {program.summary} Build your capability alongside mentors, with reflective practice and peer communities.
            </p>
            <Card className="border-dashed">
              <CardHeader>
                <CardTitle className="flex items-center gap-2 text-lg">
                  <BookOpen className="h-5 w-5" /> Competencies you will master
                </CardTitle>
              </CardHeader>
              <CardContent>
                <ul className="grid gap-2 md:grid-cols-2">
                  {program.competencies.map((item) => (
                    <li key={item} className="flex items-center gap-2 text-sm">
                      <BadgeCheck className="h-4 w-4 text-primary" aria-hidden="true" /> {item}
                    </li>
                  ))}
                </ul>
              </CardContent>
            </Card>
            {program.hasVirtualLab ? (
              <div className="flex items-start gap-3 rounded-2xl border border-primary/40 bg-primary/5 p-4 text-sm leading-6">
                <AlertCircle className="mt-0.5 h-5 w-5 text-primary" aria-hidden="true" />
                <div>
                  <p className="font-semibold">Includes hosted virtual lab sessions.</p>
                  <p className="text-muted-foreground">
                    Prepare by reviewing the safety playbook and ensure your workspace meets the performance checklist.
                  </p>
                </div>
              </div>
            ) : null}
          </div>
        </TabsContent>
        <TabsContent value="modules">
          <div className="space-y-4">
            {relatedModules.length ? (
              relatedModules.map((module) => (
                <Card key={module.id}>
                  <CardHeader>
                    <CardTitle className="text-lg">{module.title}</CardTitle>
                    <CardDescription>Status: {module.status.replace("-", " ")}</CardDescription>
                  </CardHeader>
                  <CardContent>
                    <p className="text-sm text-muted-foreground">
                      Progress {module.progress}% · Next step: {module.nextStep ?? "Keep learning"}
                    </p>
                  </CardContent>
                </Card>
              ))
            ) : (
              <p className="text-sm text-muted-foreground">Module outline will be published soon.</p>
            )}
          </div>
        </TabsContent>
        <TabsContent value="assessments">
          <div className="space-y-4">
            {relatedAssessments.length ? (
              relatedAssessments.map((assessment) => (
                <Card key={assessment.id}>
                  <CardHeader>
                    <CardTitle className="text-lg">{assessment.title}</CardTitle>
                    <CardDescription>{assessment.type === "quiz" ? "Auto-graded quiz" : "Capstone project"}</CardDescription>
                  </CardHeader>
                  <CardContent className="flex items-center justify-between">
                    <div className="text-sm text-muted-foreground">
                      Due {new Date(assessment.dueDate).toLocaleDateString()} · Status {assessment.status}
                    </div>
                    <Button variant="outline" asChild>
                      <Link href={`/assessment/${assessment.id}`}>View</Link>
                    </Button>
                  </CardContent>
                </Card>
              ))
            ) : (
              <p className="text-sm text-muted-foreground">Assessment brief will appear closer to the cohort start.</p>
            )}
          </div>
        </TabsContent>
        <TabsContent value="certifications">
          <div className="space-y-4">
            <Card>
              <CardHeader>
                <CardTitle className="text-lg flex items-center gap-2">
                  <ListChecks className="h-5 w-5" /> Certification pathway
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-3 text-sm text-muted-foreground">
                <p>Complete all modules, labs, and the capstone project to earn the SkillForge certificate.</p>
                <p>Credential is issued to your wallet within 48 hours of completion.</p>
                <Button variant="secondary" onClick={() => router.push("/credentials")}>
                  View credential wallet
                </Button>
              </CardContent>
            </Card>
          </div>
        </TabsContent>
      </Tabs>
    </div>
  );
}
