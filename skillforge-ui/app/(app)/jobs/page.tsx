"use client";

import * as React from "react";
import { Briefcase, MapPin } from "lucide-react";

import { PageHeader } from "@/components/page-header";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Textarea } from "@/components/ui/textarea";
import { useToast } from "@/components/ui/use-toast";
import { jobs } from "@/mock-api/jobs";

const uniqueRoles = Array.from(new Set(jobs.map((job) => job.role)));
const uniqueCerts = Array.from(new Set(jobs.flatMap((job) => job.requiredCerts)));

export default function JobsPage() {
  const { addToast } = useToast();
  const [filters, setFilters] = React.useState({ role: "", location: "", cert: "" });
  const [activeJob, setActiveJob] = React.useState<string | null>(null);
  const [includeCredential, setIncludeCredential] = React.useState(true);

  const filteredJobs = jobs.filter((job) => {
    const matchesRole = filters.role ? job.role === filters.role : true;
    const matchesLocation = filters.location ? job.location === filters.location : true;
    const matchesCert = filters.cert ? job.requiredCerts.includes(filters.cert) : true;
    return matchesRole && matchesLocation && matchesCert;
  });

  const handleApply = (event: React.FormEvent) => {
    event.preventDefault();
    addToast({ title: "Application sent", description: includeCredential ? "Credential link included." : "Submitted without credential." });
    setActiveJob(null);
  };

  return (
    <div className="space-y-6">
      <PageHeader
        title="Jobs & internships"
        description="Opportunities matched to your SkillForge achievements."
      />
      <Card>
        <CardHeader>
          <CardTitle className="text-xl">Filters</CardTitle>
        </CardHeader>
        <CardContent className="grid gap-4 md:grid-cols-3">
          <Select value={filters.role} onValueChange={(value) => setFilters((prev) => ({ ...prev, role: value }))}>
            <SelectTrigger aria-label="Filter by role">
              <SelectValue placeholder="Role" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="">All roles</SelectItem>
              {uniqueRoles.map((role) => (
                <SelectItem key={role} value={role}>
                  {role}
                </SelectItem>
              ))}
            </SelectContent>
          </Select>
          <Select value={filters.location} onValueChange={(value) => setFilters((prev) => ({ ...prev, location: value }))}>
            <SelectTrigger aria-label="Filter by location">
              <SelectValue placeholder="Location" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="">Anywhere</SelectItem>
              <SelectItem value="Remote">Remote</SelectItem>
              <SelectItem value="On-site">On-site</SelectItem>
            </SelectContent>
          </Select>
          <Select value={filters.cert} onValueChange={(value) => setFilters((prev) => ({ ...prev, cert: value }))}>
            <SelectTrigger aria-label="Filter by credential">
              <SelectValue placeholder="Required credential" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="">Any credential</SelectItem>
              {uniqueCerts.map((cert) => (
                <SelectItem key={cert} value={cert}>
                  {cert}
                </SelectItem>
              ))}
            </SelectContent>
          </Select>
        </CardContent>
      </Card>
      <div className="grid gap-6 md:grid-cols-2">
        {filteredJobs.map((job) => (
          <Card key={job.id} id={job.id} className="flex flex-col justify-between">
            <CardHeader>
              <CardTitle className="text-xl">{job.title}</CardTitle>
              <CardDescription>{job.company}</CardDescription>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="flex items-center gap-2 text-sm text-muted-foreground">
                <Briefcase className="h-4 w-4" aria-hidden="true" /> {job.role}
              </div>
              <div className="flex items-center gap-2 text-sm text-muted-foreground">
                <MapPin className="h-4 w-4" aria-hidden="true" /> {job.location}
              </div>
              <p className="text-sm text-muted-foreground">{job.description}</p>
              <div className="flex flex-wrap gap-2">
                {job.requiredCerts.map((cert) => (
                  <Badge key={cert} variant="outline">
                    {cert}
                  </Badge>
                ))}
              </div>
            </CardContent>
            <div className="px-6 pb-6">
              <Button className="w-full" onClick={() => setActiveJob(job.id)}>
                Apply
              </Button>
            </div>
          </Card>
        ))}
      </div>
      {activeJob ? (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 px-4">
          <Card className="w-full max-w-lg">
            <CardHeader>
              <CardTitle>Submit application</CardTitle>
              <CardDescription>Attach resume and confirm sharing your credential.</CardDescription>
            </CardHeader>
            <CardContent>
              <form className="space-y-4" onSubmit={handleApply}>
                <div className="space-y-2">
                  <Label htmlFor="resume">Resume</Label>
                  <Input id="resume" type="file" />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="note">Message</Label>
                  <Textarea id="note" placeholder="Optional note to the hiring manager" />
                </div>
                <label className="flex items-center gap-2 text-sm">
                  <input
                    type="checkbox"
                    checked={includeCredential}
                    onChange={(event) => setIncludeCredential(event.target.checked)}
                  />
                  Include credential link
                </label>
                <div className="flex items-center justify-end gap-3">
                  <Button type="button" variant="ghost" onClick={() => setActiveJob(null)}>
                    Cancel
                  </Button>
                  <Button type="submit">Submit</Button>
                </div>
              </form>
            </CardContent>
          </Card>
        </div>
      ) : null}
    </div>
  );
}
